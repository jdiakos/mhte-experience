package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.dto.*;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsAlreadyAssignedException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectSubcontractor;
import com.iknowhow.mhte.projectsexperience.domain.entities.QProject;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectSubcontractorRepository;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectCustomValidationException;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import com.iknowhow.mhte.projectsexperience.utils.Utils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
	
    private final ProjectRepository projectRepo;
    private final Utils utils;
    private final ContractService contractService;
    private final ProjectContractorService projectContractorService;
    private final ProjectSubcontractorService projectSubcontractorService;
    private final ProjectSubcontractorRepository subcontractorRepo;
    private final CommentService commentService;
    private final ProjectDocumentService projectDocumentService;


    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepo,
                              Utils utils,
                              ContractService contractService,
                              ProjectContractorService projectContractorService,
                              ProjectSubcontractorService projectSubcontractorService,
                              CommentService commentService,
                              ProjectDocumentService projectDocumentService,
                              ProjectSubcontractorRepository subcontractorRepo) {
        this.projectRepo = projectRepo;
        this.utils = utils;
        this.contractService = contractService;
        this.projectContractorService = projectContractorService;
        this.projectSubcontractorService = projectSubcontractorService;
        this.commentService = commentService;
        this.projectDocumentService = projectDocumentService;
        this.subcontractorRepo = subcontractorRepo;
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
    	
    	Project project;
        validateProjectNegativeValues(dto.getFinancialElements());
        validateTotalProjectContractorPercentages(dto);
        if(dto.getProjectDescription().getId() != null) {
        	project = projectRepo.findById(dto.getProjectDescription().getId()).orElseThrow(() ->
        		new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));
        } else {
            project = utils.initModelMapperStrict().map(dto.getProjectDescription(), Project.class);
            project.setDateCreated(LocalDateTime.now());
            validateDuplicateProjectContractor(dto);
            validateDuplicateProjectSubcontractor(dto);
        }
        validateContractNegativeValues(dto);
        
        utils.initModelMapperStrict().map(dto.getFinancialElements(), project);
        project.setLastModifiedBy("dude");
//      project.setLastModifiedBy(userPrincipal.getUsername());
        project.setProjectContractors(
                projectContractorService.assignContractorsToProject(dto.getProjectContractors(), project, userPrincipal)
        );
        project.setProjectSubcontractors(
                projectSubcontractorService.assignSubcontractorsToProject(dto.getProjectSubcontractors(), subcontractorFiles,
        		project, userPrincipal)
        );
        project.setContracts(
                contractService.createContracts(dto.getContracts(), contractFiles, project, userPrincipal)
        );
        project.setComments(
                commentService.createComments(dto.getProjectComments(), project, userPrincipal)
        );
        project.setProjectDocuments(
                projectDocumentService.createDocuments(documents, project, userPrincipal)
        );
        projectRepo.save(project);
    }

    @Override
    public void updateProject(MhteUserPrincipal userPrincipal, ProjectMasterDTO dto,
    		MultipartFile[] subcontractorFiles, MultipartFile[] contractFiles, MultipartFile[] documents) {
    	
    	Project project = projectRepo.findById(dto.getProjectDescription().getId()).orElseThrow(() ->
        new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));
    	
        validateProjectNegativeValues(dto.getFinancialElements());
        validateTotalProjectContractorPercentages(dto);
        validateContractNegativeValues(dto);
        
        List<Long> oldIds = project.getProjectSubcontractors().
        		stream().
        		map(ProjectSubcontractor::getId).toList();
        List<Long> newIds = dto.getProjectSubcontractors()
                .stream()
                .filter(s -> s.getId() != null)
                .map(ProjectSubcontractorDTO::getId)
                .toList();
        List<Long> differences = oldIds.stream()
                .filter(element -> !newIds.contains(element))
                .collect(Collectors.toList());
        utils.initModelMapperStrict().map(dto.getProjectDescription(), project);
        utils.initModelMapperStrict().map(dto.getFinancialElements(), project);
        subcontractorRepo.deleteAllById(differences);
        project.setLastModifiedBy("dude");
//      project.setLastModifiedBy(userPrincipal.getUsername());
        project.setProjectContractors(
                projectContractorService.assignContractorsToProject(dto.getProjectContractors(), project, userPrincipal)
        );
        project.setProjectSubcontractors(
                projectSubcontractorService.assignSubcontractorsToProject(dto.getProjectSubcontractors(), subcontractorFiles,
        		project, userPrincipal)
        );
        project.setContracts(
                contractService.createContracts(dto.getContracts(), contractFiles, project, userPrincipal)
        );
        project.setComments(
                commentService.createComments(dto.getProjectComments(), project, userPrincipal)
        );
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

        if (dto.getAdam() != null) {
            booleanBuilder.and(qProject.adam.eq(dto.getAdam()));
        }

        if (dto.getProtocolNumber() != null) {
            booleanBuilder.and(qProject.protocolNumber.eq(dto.getProtocolNumber()));
        }

        if (dto.getResponsibleEntity() != null) {
            booleanBuilder.and(qProject.responsibleEntity.eq(dto.getResponsibleEntity()));
        }

        if (dto.getProjectCategory() != null) {
            booleanBuilder.and(qProject.projectCategory.eq(dto.getProjectCategory()));
        }
        
        return projectRepo.findAll(booleanBuilder, pageable)
                .map(this::toProjectResponseDTO);
    }

    private ProjectResponseDTO toProjectResponseDTO(Project project) {
        ModelMapper mapper = utils.initModelMapperStrict();
        ProjectResponseDTO dto = new ProjectResponseDTO();
        ProjectDescriptionResponseDTO descriptionDTO = mapper.map(project, ProjectDescriptionResponseDTO.class);
        ProjectFinancialElementsDTO financialsDTO = mapper.map(project, ProjectFinancialElementsDTO.class);
        List<ProjectContractorResponseDTO> contractorsDTOList = project.getProjectContractors()
                .stream()
                .map(contractor -> mapper.map(contractor, ProjectContractorResponseDTO.class))
                .toList();
        List<ProjectSubcontractorResponseDTO> subcontractorDTOList = project.getProjectSubcontractors()
                .stream()
                .map(subcontractor -> mapper.map(subcontractor, ProjectSubcontractorResponseDTO.class))
                .toList();
        List<ProjectDocumentsResponseDTO> documentsDTOList = project.getProjectDocuments()
                .stream()
                .map(projectDocument -> mapper.map(projectDocument, ProjectDocumentsResponseDTO.class))
                .toList();
        List<CommentsResponseDTO> commentsDTOList = project.getComments()
                .stream()
                .map(comment -> mapper.map(comment, CommentsResponseDTO.class))
                .toList();

        dto.setProjectDescription(descriptionDTO);
        dto.setProjectFinancialElements(financialsDTO);
        dto.setProjectContractors(contractorsDTOList);
        dto.setProjectSubcontractors(subcontractorDTOList);
        dto.setDocuments(documentsDTOList);
        dto.setComments(commentsDTOList);

        return dto;
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
}
