package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.dto.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.QProject;
import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectCustomValidationException;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import com.iknowhow.mhte.projectsexperience.utils.Utils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ProjectServiceImpl implements ProjectService {
	
    Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
	
    private final ProjectRepository projectRepo;
    private final Utils utils;
    private final ContractService contractService;
    private final ProjectContractorService projectContractorService;
    private final ProjectSubcontractorService projectSubcontractorService;


    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepo,
                              Utils utils,
                              ContractService contractService,
                              ProjectContractorService projectContractorService,
                              ProjectSubcontractorService projectSubcontractorService) {
        this.projectRepo = projectRepo;
        this.utils = utils;
        this.contractService = contractService;
        this.projectContractorService = projectContractorService;
        this.projectSubcontractorService = projectSubcontractorService;
    }
    
    @Override
    public Page<ProjectResponseDTO> fetchAllProjects(Pageable page){
    	logger.info("fetch all projects service");
    	ModelMapper loose = utils.initModelMapperLoose();
        return projectRepo.findAll(page).map(project -> loose.map(project, ProjectResponseDTO.class));
    }
    
    @Override
    public ProjectResponseDTO getProjectById(Long id) {
    	logger.info("get project by id service");
    	ModelMapper loose = utils.initModelMapperLoose();
    	ProjectResponseDTO response = projectRepo.findById(id).map(project ->
    		loose.map(project, ProjectResponseDTO.class)).orElseThrow(() ->
    			new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));
    	return response;
    }
    
    @Override
    public ProjectResponseDTO getProjectByContractId(Long id) {
    	logger.info("get project by contract id service");
    	ModelMapper loose = utils.initModelMapperLoose();
    	return projectRepo.findByContracts_Id(id).map(project -> loose.map(project, ProjectResponseDTO.class)).orElseThrow(()->
    		new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND)
    	);
    }
    
    @Override
    public ProjectResponseDTO getProjectByAdam(String adam) {
    	logger.info("get project by adam service");
    	ModelMapper loose = utils.initModelMapperLoose();
    	return projectRepo.findByAdam(adam).map(project -> loose.map(project, ProjectResponseDTO.class)).orElseThrow(()->
    		new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND)
    	);
    };
    
    @Override
    public Page<ProjectResponseDTO> getProjectByCategory(ProjectsCategoryEnum category, Pageable page) {
    	logger.info("get project by category service");
    	ModelMapper loose = utils.initModelMapperLoose();
    	return projectRepo.findByProjectCategory(category, page).map(project -> loose.map(project, ProjectResponseDTO.class));
    }
    
    @Override
    public ProjectResponseDTO getProjectByProtocolNumber(String number) {
    	logger.info("get project by category service");
    	ModelMapper loose = utils.initModelMapperLoose();
    	return loose.map(projectRepo.findByProtocolNumber(number), ProjectResponseDTO.class);
    }
    
    @Override
    public Page<ProjectResponseDTO> getProjectByResponsibleEntity(String entity, Pageable page) {
    	logger.info("get project by category service");
    	ModelMapper loose = utils.initModelMapperLoose();
    	return projectRepo.findByResponsibleEntity(entity, page).map(project -> loose.map(project, ProjectResponseDTO.class));
    }

    @Override
    @Transactional
    public void createProject(ProjectMasterDTO dto, MhteUserPrincipal userPrincipal) {
//        if(!validateProject(dto)) {
//            throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.VALUES_CANNOT_BE_NEGATIVE);
//        }

        Project project = utils.initModelMapperStrict().map(dto, Project.class);

        project.setDateCreated(LocalDateTime.now());
        //@TODO - PLACEHOLDER: CHANGE WITH USER PRINCIPAL
        project.setLastModifiedBy("dude");
//        project.setLastModifiedBy(userPrincipal.getUsername());

        projectRepo.save(project);
        logger.info("PROJECT ADDED");

        projectContractorService.assignContractorsToProject(dto.getProjectContractors(), project, userPrincipal);
        logger.info("PROJECT CONTRACTORS ADDED");

        // @TODO - SAVE FILES
        projectSubcontractorService.assignSubcontractorsToProject(dto.getProjectSubcontractors(), project, userPrincipal);
        logger.info("PROJECT SUBCONTRACTORS ADDED");

        // @TODO - SAVE FILES
        contractService.createContracts(dto.getContracts(), project, userPrincipal);
        logger.info("CONTRACTS ADDED");

    }

    @Override
    public ProjectResponseDTO updateProject(CUDProjectDTO dto) {
    	logger.info("update project service");

    	ModelMapper loose = utils.initModelMapperLoose();
    	if(!validateProject(dto)) {
    		throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.VALUES_CANNOT_BE_NEGATIVE);
    	}
        Project projectExists = projectRepo.findByAdam(dto.getAdam()).orElseThrow(()->
        	new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND)
        );
        
        Long id = projectExists.getId();
        projectExists = loose.map(dto, Project.class);
        projectExists.setId(id);
        //@TODO - PLACEHOLDER: CHANGE WITH USER PRINCIPAL
        projectExists.setLastModifiedBy("dude");
        projectRepo.save(projectExists);
        ProjectResponseDTO response = loose.map(projectExists, ProjectResponseDTO.class);
        return response;
    }

    @Override
    public CUDProjectDTO deleteProject(Long id) {
    	logger.info("delete project service");

    	ModelMapper strict = utils.initModelMapperStrict();

        Project projectExists = projectRepo.findById(id).orElseThrow(()->
        	new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND)
        );
        CUDProjectDTO response = strict.map(projectExists, CUDProjectDTO.class);
        //@TODO - PLACEHOLDER: CHANGE WITH USER PRINCIPAL
        projectExists.setLastModifiedBy("dude");
        projectRepo.delete(projectExists);
        return response;
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
        
        ModelMapper modelMapper = utils.initModelMapperLoose();
        return projectRepo.findAll(booleanBuilder, pageable)
                .map(project -> modelMapper.map(project, ProjectResponseDTO.class));
    }
    
    private boolean validateProject(CUDProjectDTO dto) {
    	if(dto.getInitialContractBudget() > 0 &&
    			dto.getInitialContractValue() > 0 &&
    			dto.getSupplementaryContractValue() > 0 &&
    			dto.getApeValue() > 0 &&
    			dto.getTotalValue() > 0) {
    		return true;
    	}
    	return false;
    }
}
