package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.enums.ContractTypeEnum;
import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import com.iknowhow.mhte.projectsexperience.dto.*;
import com.iknowhow.mhte.projectsexperience.dto.feign.CompanyDTO;
import com.iknowhow.mhte.projectsexperience.exception.*;
import com.iknowhow.mhte.projectsexperience.feign.CompaniesFeignClient;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.query.AuditEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.iknowhow.mhte.projectsexperience.domain.entities.*;
import com.iknowhow.mhte.projectsexperience.domain.entities.QProject;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.utils.Utils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepo;
    private final Utils utils;
    private final ContractService contractService;
    private final ProjectContractorService projectContractorService;
    private final ProjectSubcontractorService projectSubcontractorService;
    private final CommentService commentService;
    private final ProjectDocumentService projectDocumentService;
    private final ExperienceService experienceService;
    private final CompaniesFeignClient companiesFeignClient;
    private final EntityManager entityManager;


    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepo,
                              Utils utils,
                              ContractService contractService,
                              ProjectContractorService projectContractorService,
                              ProjectSubcontractorService projectSubcontractorService,
                              CommentService commentService,
                              ProjectDocumentService projectDocumentService,
                              ExperienceService experienceService,
                              CompaniesFeignClient companiesFeignClient,
                              EntityManager entityManager) {
        this.projectRepo = projectRepo;
        this.utils = utils;
        this.contractService = contractService;
        this.projectContractorService = projectContractorService;
        this.projectSubcontractorService = projectSubcontractorService;
        this.commentService = commentService;
        this.projectDocumentService = projectDocumentService;
        this.experienceService = experienceService;
        this.companiesFeignClient = companiesFeignClient;
        this.entityManager = entityManager;
    }
    
    @Override
    public Page<ProjectDTO> fetchAllProjects(Pageable pageable){
        return projectRepo.findAll(pageable)
                .map(this::toProjectDTO);
    }

    @Override
    @Transactional
    public void createProject(MhteUserPrincipal userPrincipal, ProjectDTO dto,
    		MultipartFile[] subcontractorFiles, MultipartFile[] contractFiles, MultipartFile[] documents) {

    	validateProjectData(dto, subcontractorFiles, contractFiles, documents);

        // PROJECT DETAILS
        dto.getProjectDescription().setId(null);
        Project project = utils.initModelMapperStrict().map(dto.getProjectDescription(), Project.class);
        utils.initModelMapperStrict().map(dto.getProjectFinancialElements(), project);
        project.setDateCreated(LocalDateTime.now());
        project.setLastModifiedBy(userPrincipal.getUsername());

        // DEPENDANT ENTITIES
        project.setProjectContractors(
                projectContractorService.assignContractorsToProject(dto.getProjectContractors(),project, userPrincipal)
        );
        project.setProjectSubcontractors(
                projectSubcontractorService.assignSubcontractorsToProject(dto.getProjectSubcontractors(), subcontractorFiles,project, userPrincipal)
        );
        project.setContracts(
                contractService.assignContractsToProject(dto.getContracts(), contractFiles, project, userPrincipal)
        );
        project.setComments(
                commentService.assignCommentsToProject(dto.getProjectComments(), project, userPrincipal)
        );
        project.setProjectDocuments(
                projectDocumentService.assignDocumentsToProject(dto.getProjectDocuments(), documents, project, userPrincipal)
        );
        project.setExperiences(
                experienceService.assignExperienceToProject(dto.getProjectExperience(), project, userPrincipal)
        );

        try {
            projectRepo.save(project);
        } catch (Exception e){
        	log.error(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateProject(MhteUserPrincipal userPrincipal, ProjectDTO dto,
    		MultipartFile[] subcontractorFiles, MultipartFile[] contractFiles, MultipartFile[] documents) {

        validateProjectData(dto, subcontractorFiles, contractFiles, documents);

    	Project project = projectRepo.findById(dto.getProjectDescription().getId()).orElseThrow(() ->
        	new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));
    	

        // PROJECT DETAILS
        utils.initModelMapperStrict().map(dto.getProjectDescription(), project);
        utils.initModelMapperStrict().map(dto.getProjectFinancialElements(), project);
        project.setLastModifiedBy(userPrincipal.getUsername());

        // DEPENDANT ENTITIES
        project.addContractors(
                projectContractorService.assignContractorsToProject(dto.getProjectContractors(), project, userPrincipal)
        );
        project.addSubcontractor(
                projectSubcontractorService.assignSubcontractorsToProject(dto.getProjectSubcontractors(), subcontractorFiles, project, userPrincipal)
        );
        project.addContracts(
                contractService.assignContractsToProject(dto.getContracts(), contractFiles, project, userPrincipal)
        );
        project.addComments(
                commentService.assignCommentsToProject(dto.getProjectComments(), project, userPrincipal)
        );
        project.addProjectDocuments(
                projectDocumentService.assignDocumentsToProject(dto.getProjectDocuments(), documents, project, userPrincipal)
        );
        project.addExperiences(
                experienceService.assignExperienceToProject(dto.getProjectExperience(), project, userPrincipal)
        );

        /*   works!!!!!!!!
			projectContractorService.dischargeContractors(project, dto);
			project.getProjectContractors().clear();
			project.getProjectContractors().addAll(contractors);
		*/

        projectRepo.save(project);
    }

    @Override
    public void deleteProject(Long id, MhteUserPrincipal userPrincipal) {
        Project projectExists = projectRepo.findById(id).orElseThrow(()->
        	new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND)
        );
        projectExists.setLastModifiedBy(userPrincipal.getUsername());
        projectRepo.delete(projectExists);
    }
    
    @Override
    public Page<ProjectDTO> search(ProjectSearchDTO dto, Pageable pageable) {
        QProject qProject = QProject.project;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        Optional.ofNullable(dto.getTitle())
                .filter(Predicate.not(String::isEmpty))
                .map(qProject.title::containsIgnoreCase)
                .ifPresent(booleanBuilder::and);
        Optional.ofNullable(dto.getAdam())
                .filter(Predicate.not(String::isEmpty))
                .map(qProject.adam::eq)
                .ifPresent(booleanBuilder::and);
        Optional.ofNullable(dto.getProtocolNumber())
                .filter(Predicate.not(String::isEmpty))
                .map(qProject.protocolNumber::eq)
                .ifPresent(booleanBuilder::and);
        Optional.ofNullable(dto.getResponsibleEntity())
                .filter(Predicate.not(String::isEmpty))
                .map(qProject.responsibleEntity::containsIgnoreCase)
                .ifPresent(booleanBuilder::and);
        Optional.ofNullable(dto.getProjectCategory())
                .map(qProject.projectCategory::eq)
                .ifPresent(booleanBuilder::and);

        return projectRepo.findAll(booleanBuilder, pageable)
                .map(this::toProjectDTO);
    }


    @Override
    public ProjectDTO getProjectById(Long projectId) {
        Project project = projectRepo.findById(projectId).orElseThrow(()->
                new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND)
        );

        return toProjectDTO(project);
    }

    @Override
    public List<AuditHistoryDTO> getProjectAuditHistory(Long projectId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Object[]> history = auditReader.createQuery()
                .forRevisionsOfEntityWithChanges(Project.class, true)
                .add(AuditEntity.id().eq(projectId))
                .getResultList();

        return history
                .stream()
                .map(obj -> {
                    Project project = (Project) obj[0];
                    DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) obj[1];

                    AuditHistoryDTO dto = new AuditHistoryDTO();
                    dto.setRevisionNumber(revisionEntity.getId());
                    dto.setLastModifiedBy(project.getLastModifiedBy());
                    dto.setLastModificationDate(project.getLastModificationDate());
                    return dto;
                })
                .toList();
    }

    @Override
    public ProjectDTO getProjectAuditByRevisionNumber(Integer revisionNumber) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        Project project = (Project) auditReader
                .createQuery()
                .forEntitiesAtRevision(Project.class, revisionNumber)
                .getResultList()
                .get(0);

        return toAuditProjectDTO(project, revisionNumber);
    }


    /**
     * MAPPERS
     */

    private ProjectDTO toProjectDTO(Project project) {
        ModelMapper mapper = utils.initModelMapperStrict();
        ProjectDTO dto = new ProjectDTO();

        // MAP PROJECT DATA
        ProjectDescriptionDTO descriptionDTO = mapper.map(project, ProjectDescriptionDTO.class);
        ProjectFinancialElementsDTO financialsDTO = mapper.map(project, ProjectFinancialElementsDTO.class);

        // MAP DEPENDANT ENTITIES
        List<ProjectContractorDTO> contractorsDTOList = project.getProjectContractors()
                .stream()
                .map(contractor -> mapper.map(contractor, ProjectContractorDTO.class))
                .toList();
        // @TODO -> THIS MIGHT NEED TO BE CACHED FOR GET ALL PROJECTS, TO BE CHECKED LATER
        getContractorNames(contractorsDTOList);
        List<ProjectSubcontractorDTO> subcontractorDTOList = project.getProjectSubcontractors()
                .stream()
                .map(subcontractor -> mapper.map(subcontractor, ProjectSubcontractorDTO.class))
                .toList();
        // @TODO -> THIS MIGHT NEED TO BE CACHED FOR GET ALL PROJECTS, TO BE CHECKED LATER
        getSubcontractorNames(subcontractorDTOList);
        List<ContractDTO> contractDTOList = project.getContracts()
                .stream()
                .map(contract -> mapper.map(contract, ContractDTO.class))
                .toList();
        List<ProjectDocumentsDTO> documentsDTOList = project.getProjectDocuments()
                .stream()
                .map(projectDocument -> mapper.map(projectDocument, ProjectDocumentsDTO.class))
                .toList();
        List<CommentsDTO> commentsDTOList = project.getComments()
                .stream()
                .map(this::toCommentDTO)
                .toList();
        List<ExperienceDTO> experienceDTOList = project.getExperiences()
                .stream()
                .map(experience -> mapper.map(experience, ExperienceDTO.class))
                .toList();

        dto.setProjectDescription(descriptionDTO);
        dto.setProjectFinancialElements(financialsDTO);
        dto.setProjectContractors(contractorsDTOList);
        dto.setProjectSubcontractors(subcontractorDTOList);
        dto.setContracts(contractDTOList);
        dto.setProjectDocuments(documentsDTOList);
        dto.setProjectComments(commentsDTOList);
        dto.setProjectExperience(experienceDTOList);

        return dto;
    }

    private CommentsDTO toCommentDTO (Comment comment) {
        // we need this because the field names between the dto and the entity differ, so the mapper ignores them
        CommentsDTO dto = new CommentsDTO();
        dto.setId(comment.getId());
        dto.setMessage(comment.getMessage());
        dto.setUsername(comment.getCreatedBy());
        dto.setDate(comment.getCreatedAt());
        dto.setRole(comment.getRole());
        return dto;
    }


    private ProjectDTO toAuditProjectDTO(Project project, Integer revisionNumber) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        ModelMapper mapper = utils.initModelMapperStrict();
        ProjectDTO dto = new ProjectDTO();

        dto.setProjectDescription(mapper.map(project, ProjectDescriptionDTO.class));
        dto.setProjectFinancialElements(mapper.map(project, ProjectFinancialElementsDTO.class));

        // contractors
        if (!project.getProjectContractors().isEmpty()) {
            List<ProjectContractor> contractors = (List<ProjectContractor>) auditReader
                    .createQuery()
                    .forEntitiesAtRevision(ProjectContractor.class, revisionNumber)
                    .getResultList();
            List<ProjectContractorDTO> projectContractorDTOs = contractors
                    .stream()
                    .map(contractor -> mapper.map(contractor, ProjectContractorDTO.class))
                    .toList();
            getContractorNames(projectContractorDTOs);
            dto.setProjectContractors(projectContractorDTOs);
        }

        // subcontractors
        if (!project.getProjectSubcontractors().isEmpty()) {
            List<ProjectSubcontractor> subcontractors = (List<ProjectSubcontractor>) auditReader
                    .createQuery()
                    .forEntitiesAtRevision(ProjectSubcontractor.class, revisionNumber)
                    .getResultList();
            List<ProjectSubcontractorDTO> subcontractorDTOs = subcontractors
                    .stream()
                    .map(subcontractor -> mapper.map(subcontractor, ProjectSubcontractorDTO.class))
                    .toList();
            getSubcontractorNames(subcontractorDTOs);
            dto.setProjectSubcontractors(subcontractorDTOs);
        }

        // contracts
        if (!project.getContracts().isEmpty()) {
            List<Contract> contracts = (List<Contract>) auditReader
                    .createQuery()
                    .forEntitiesAtRevision(Contract.class, revisionNumber)
                    .getResultList();
            List<ContractDTO> contractDTOs = contracts
                    .stream()
                    .map(contract -> mapper.map(contract, ContractDTO.class))
                    .toList();
            dto.setContracts(contractDTOs);
        }

        // documents
        if (!project.getProjectDocuments().isEmpty()) {
            List<ProjectDocument> documents = (List<ProjectDocument>) auditReader
                    .createQuery()
                    .forEntitiesAtRevision(ProjectDocument.class, revisionNumber)
                    .getResultList();
            List<ProjectDocumentsDTO> projectDocumentsDTOs = documents
                    .stream()
                    .map(projectDocument -> mapper.map(projectDocument, ProjectDocumentsDTO.class))
                    .toList();
            dto.setProjectDocuments(projectDocumentsDTOs);
        }

        // experiences
        if (!project.getExperiences().isEmpty()) {
            List<Experience> experiences = (List<Experience>) auditReader
                    .createQuery()
                    .forEntitiesAtRevision(Experience.class, revisionNumber)
                    .getResultList();
            List<ExperienceDTO> experienceDTOs = experiences
                    .stream()
                    .map(experience -> mapper.map(experience, ExperienceDTO.class))
                    .toList();
            dto.setProjectExperience(experienceDTOs);
        }

        return dto;
    }

    /**
     * FEIGN MAPPERS
     */
    private void getContractorNames(List<ProjectContractorDTO> contractors) {
        List<Long> contractorIds = contractors.stream().map(ProjectContractorDTO::getContractorId).toList();
        List<CompanyDTO> companies = companiesFeignClient.getNamesByIds(contractorIds);
        contractors.forEach(contractor -> {
            companies
                    .stream()
                    .filter(company -> company.getId().equals(contractor.getContractorId()))
                    .findFirst()
                    .ifPresent(companyDTO ->
                            contractor.setContractorName(companyDTO.getCompanyInfo().getCompanyName()));
        });

    }

    private void getSubcontractorNames(List<ProjectSubcontractorDTO> subcontractors) {
        List<Long> subcontractorIds = subcontractors.stream().map(ProjectSubcontractorDTO::getSubcontractorId).toList();
        List<CompanyDTO> companies = companiesFeignClient.getNamesByIds(subcontractorIds);
        subcontractors.forEach(subcontractor -> {
            companies
                    .stream()
                    .filter(company -> company.getId().equals(subcontractor.getSubcontractorId()))
                    .findFirst()
                    .ifPresent(companyDTO ->
                            subcontractor.setSubcontractorName(companyDTO.getCompanyInfo().getCompanyName()));
        });
    }


    /**
     * VALIDATORS
     */

    private void validateProjectData(ProjectDTO dto, MultipartFile[] subcontractorFiles,
                                     MultipartFile[] contractFiles, MultipartFile[] documents) {

        validateProjectNegativeValues(dto.getProjectFinancialElements());
        validateTotalProjectContractorPercentages(dto);
        validateDuplicateProjectContractor(dto);
        validateDuplicateProjectSubcontractor(dto);
        validateContractNegativeValues(dto);
        validateFileExtensions(subcontractorFiles);
        validateFileExtensions(contractFiles);
        validateFileExtensions(documents);
        validateAdamForPublicProjects(dto.getProjectDescription());
        validateInitialContract(dto.getContracts());

    }

    private void validateProjectNegativeValues(ProjectFinancialElementsDTO dto) {
        if (dto.getInitialContractBudget()!=null && dto.getInitialContractBudget() < 0 &&
        		dto.getInitialContractValue()!=null && dto.getInitialContractValue() < 0 &&
				dto.getSupplementaryContractValue()!=null && dto.getSupplementaryContractValue() < 0 &&
				dto.getApeValue()!=null && dto.getApeValue() < 0 &&
				dto.getTotalValue()!=null && dto.getTotalValue() < 0) {
            throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.VALUES_CANNOT_BE_NEGATIVE);
        }
    }

    private void validateTotalProjectContractorPercentages(ProjectDTO dto) {
        if (dto.getProjectContractors()!= null && dto.getProjectContractors()
                .stream()
                .mapToDouble(ProjectContractorDTO::getParticipationPercentage)
                .sum() > 100) {
            throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.TOTAL_PERCENTAGE_EXCEEDS_MAX);
        }
    }

    private void validateDuplicateProjectContractor(ProjectDTO dto) {
        Set<Long> uniqueContractors = dto.getProjectContractors()
                .stream()
                .map(ProjectContractorDTO::getContractorId)
                .collect(Collectors.toSet());

        if (dto.getProjectContractors() != null && dto.getProjectContractors().size() != uniqueContractors.size()) {
            throw new MhteProjectsAlreadyAssignedException(MhteProjectErrorMessage.ALREADY_ASSIGNED.name());
        }
    }

    private void validateDuplicateProjectSubcontractor(ProjectDTO dto) {
        Set<Long> uniqueSubcontractors = dto.getProjectSubcontractors()
                .stream()
                .map(ProjectSubcontractorDTO::getSubcontractorId)
                .collect(Collectors.toSet());
        if (dto.getProjectSubcontractors() != null && dto.getProjectSubcontractors().size() != uniqueSubcontractors.size()) {
            throw new MhteProjectsAlreadyAssignedException(MhteProjectErrorMessage.ALREADY_ASSIGNED.name());
        }
    }

    private void validateContractNegativeValues(ProjectDTO dto) {
        if (dto != null) {
            dto.getContracts().forEach(contract -> {
                if (contract.getContractValue() < 0) {
                    throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.VALUES_CANNOT_BE_NEGATIVE);
                }
            });
        }
    }

    private void validateAdamForPublicProjects(ProjectDescriptionDTO dto) {
        // ADAM NUMBER SHOULD BE USED ONLY IN PUBLIC PROJECTS

        if (dto.getProjectCategory().equals(ProjectsCategoryEnum.PUBLIC_PROJECT) && dto.getAdam() == null) {

            throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.PUBLIC_PROJECTS_MUST_HAVE_AN_ADAM);

        } else if (!dto.getProjectCategory().equals(ProjectsCategoryEnum.PUBLIC_PROJECT) && dto.getAdam() != null) {

            throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.NON_PUBLIC_PROJECTS_CANNOT_HAVE_AN_ADAM);
        }
    }

    private void validateInitialContract(List<ContractDTO> dtoList) {
        if (dtoList != null && dtoList.size() != 0) {
            // PROJECT MUST HAVE AN INITIAL CONTRACT AND CAN'T HAVE MORE THAN ONE INITIAL CONTRACTS
            List<ContractDTO> result = dtoList
                    .stream()
                    .filter(dto -> dto.getContractType().equals(ContractTypeEnum.INITIAL_CONTRACT))
                    .toList();

            if (result.size() > 1) {
                throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.MORE_THAN_ONE_INITIAL_CONTRACTS_PRESENT);
            } else if (result.size() < 1) {
                throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.NO_INITIAL_CONTRACT_PRESENT);
            }
        }

    }

    private void validateFileExtensions(MultipartFile[] files) {
        // @TODO - LEFT TXT IN FOR TESTING, NEED TO ADD MORE ALLOWED TYPES
        List<String> allowedFileTypes = Arrays.asList("pdf", "doc", "docx", "txt");
        if(files != null && files.length>0) {
	        for (MultipartFile file: files) {
	            if (file != null) {
	                String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
	                if (!allowedFileTypes.contains(extension)) {
	                    throw new MhteProjectFileException(MhteProjectErrorMessage.FILE_TYPE_NOT_ALLOWED.name());
	                }
	            }
	        }
        }
    }

}
