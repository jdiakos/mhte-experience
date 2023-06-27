package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.enums.ContractTypeEnum;
import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import com.iknowhow.mhte.projectsexperience.dto.*;
import com.iknowhow.mhte.projectsexperience.exception.*;
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
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepo;
    private final Utils utils;
    private final ContractService contractService;
    private final ProjectContractorService projectContractorService;
    private final ProjectSubcontractorService projectSubcontractorService;
    private final CommentService commentService;
    private final ProjectDocumentService projectDocumentService;


    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepo,
                              Utils utils,
                              ContractService contractService,
                              ProjectContractorService projectContractorService,
                              ProjectSubcontractorService projectSubcontractorService,
                              CommentService commentService,
                              ProjectDocumentService projectDocumentService) {
        this.projectRepo = projectRepo;
        this.utils = utils;
        this.contractService = contractService;
        this.projectContractorService = projectContractorService;
        this.projectSubcontractorService = projectSubcontractorService;
        this.commentService = commentService;
        this.projectDocumentService = projectDocumentService;
    }
    
    @Override
    public Page<ProjectResponseDTO> fetchAllProjects(Pageable pageable){
        return projectRepo.findAll(pageable)
                .map(this::toProjectResponseDTO);
    }

    @Override
    @Transactional
    public void createProject(MhteUserPrincipal userPrincipal, ProjectMasterDTO dto,
    		MultipartFile[] subcontractorFiles, MultipartFile[] contractFiles, MultipartFile[] documents) {

    	validateProjectData(dto, subcontractorFiles, contractFiles, documents);


        // PROJECT DETAILS
        dto.getProjectDescription().setId(null);
        Project project = utils.initModelMapperStrict().map(dto.getProjectDescription(), Project.class);
        utils.initModelMapperStrict().map(dto.getProjectFinancialElements(), project);

        project.setDateCreated(LocalDateTime.now());
        // @TODO - PLACEHOLDER: CHANGE WITH USER PRINCIPAL
        project.setLastModifiedBy("dude");

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
                projectDocumentService.assignDocumentsToProject(documents, project, userPrincipal)
        );

        try {
            projectRepo.save(project);
        } catch (Exception e){
        	System.out.println(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateProject(MhteUserPrincipal userPrincipal, ProjectMasterDTO dto,
    		MultipartFile[] subcontractorFiles, MultipartFile[] contractFiles, MultipartFile[] documents) {

        validateProjectData(dto, subcontractorFiles, contractFiles, documents);

    	Project project = projectRepo.findById(dto.getProjectDescription().getId()).orElseThrow(() ->
        	new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));
    	

        // PROJECT DETAILS
        utils.initModelMapperStrict().map(dto.getProjectDescription(), project);
        utils.initModelMapperStrict().map(dto.getProjectFinancialElements(), project);
        // @TODO - PLACEHOLDER: CHANGE WITH USER PRINCIPAL
        project.setLastModifiedBy("dude");

        // DEPENDANT ENTITIES
        project.getProjectDocuments().clear();
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
                projectDocumentService.assignDocumentsToProject(documents, project, userPrincipal)
        );

        /*   works!!!!!!!!
			projectContractorService.dischargeContractors(project, dto);
			project.getProjectContractors().clear();
			project.getProjectContractors().addAll(contractors);
		*/

        projectRepo.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        Project projectExists = projectRepo.findById(id).orElseThrow(()->
        	new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND)
        );
        //@TODO - PLACEHOLDER: CHANGE WITH USER PRINCIPAL
        projectExists.setLastModifiedBy("dude");
        projectRepo.delete(projectExists);
    }
    
    @Override
    public Page<ProjectResponseDTO> search(ProjectSearchDTO dto, Pageable pageable) {
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
                .map(this::toProjectResponseDTO);
    }

    private ProjectResponseDTO toProjectResponseDTO(Project project) {
        ModelMapper mapper = utils.initModelMapperStrict();
        ProjectResponseDTO dto = new ProjectResponseDTO();

        // MAP PROJECT DATA
        ProjectDescriptionDTO descriptionDTO = mapper.map(project, ProjectDescriptionDTO.class);
        ProjectFinancialElementsDTO financialsDTO = mapper.map(project, ProjectFinancialElementsDTO.class);

        // MAP DEPENDANT ENTITIES
        List<ProjectContractorDTO> contractorsDTOList = project.getProjectContractors()
                .stream()
                .map(contractor -> mapper.map(contractor, ProjectContractorDTO.class))
                .toList();
        List<ProjectSubcontractorDTO> subcontractorDTOList = project.getProjectSubcontractors()
                .stream()
                .map(subcontractor -> mapper.map(subcontractor, ProjectSubcontractorDTO.class))
                .toList();
        List<ContractResponseDTO> contractDTOList = project.getContracts()
                .stream()
                .map(contract -> mapper.map(contract, ContractResponseDTO.class))
                .toList();
        List<ProjectDocumentsDTO> documentsDTOList = project.getProjectDocuments()
                .stream()
                .map(projectDocument -> mapper.map(projectDocument, ProjectDocumentsDTO.class))
                .toList();
        List<CommentsDTO> commentsDTOList = project.getComments()
                .stream()
                .map(this::toCommentDTO)
                .toList();

        dto.setProjectDescription(descriptionDTO);
        dto.setProjectFinancialElements(financialsDTO);
        dto.setProjectContractors(contractorsDTOList);
        dto.setProjectSubcontractors(subcontractorDTOList);
        dto.setContracts(contractDTOList);
        dto.setDocuments(documentsDTOList);
        dto.setComments(commentsDTOList);

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

    /**
     * VALIDATORS
     *
     */

    private void validateProjectData(ProjectMasterDTO dto, MultipartFile[] subcontractorFiles,
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
        if (dto.getInitialContractBudget() < 0 &&
                dto.getInitialContractValue() < 0 &&
                dto.getSupplementaryContractValue() < 0 &&
                dto.getApeValue() < 0 &&
                dto.getTotalValue() < 0) {
            throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.VALUES_CANNOT_BE_NEGATIVE);
        }
    }

    private void validateTotalProjectContractorPercentages(ProjectMasterDTO dto) {
        if (dto.getProjectContractors()
                .stream()
                .mapToDouble(ProjectContractorDTO::getParticipationPercentage)
                .sum() > 100) {
            throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.TOTAL_PERCENTAGE_EXCEEDS_MAX);
        }
    }

    private void validateDuplicateProjectContractor(ProjectMasterDTO dto) {
        Set<Long> uniqueContractors = dto.getProjectContractors()
                .stream()
                .map(ProjectContractorDTO::getContractorId)
                .collect(Collectors.toSet());

        if (dto.getProjectContractors().size() != uniqueContractors.size()) {
            throw new MhteProjectsAlreadyAssignedException(MhteProjectErrorMessage.ALREADY_ASSIGNED.name());
        }
    }

    private void validateDuplicateProjectSubcontractor(ProjectMasterDTO dto) {
        Set<Long> uniqueSubcontractors = dto.getProjectSubcontractors()
                .stream()
                .map(ProjectSubcontractorDTO::getSubcontractorId)
                .collect(Collectors.toSet());
        if (dto.getProjectSubcontractors().size() != uniqueSubcontractors.size()) {
            throw new MhteProjectsAlreadyAssignedException(MhteProjectErrorMessage.ALREADY_ASSIGNED.name());
        }
    }

    private void validateContractNegativeValues(ProjectMasterDTO dto) {
        dto.getContracts().forEach(contract -> {
            if (contract.getContractValue() < 0) {
                throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.VALUES_CANNOT_BE_NEGATIVE);
            }
        });
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

    private void validateFileExtensions(MultipartFile[] files) {
        // @TODO - LEFT TXT IN FOR TESTING, NEED TO ADD MORE ALLOWED TYPES
        List<String> allowedFileTypes = Arrays.asList("pdf", "doc", "docx", "txt");
        if(files!=null && files.length>0) {
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
