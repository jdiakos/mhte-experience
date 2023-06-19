package com.iknowhow.mhte.projectsexperience.service;

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
import com.iknowhow.mhte.projectsexperience.dto.CUDProjectDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectConDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSearchDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectCustomValidationException;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import com.iknowhow.mhte.projectsexperience.utils.Utils;
import com.querydsl.core.BooleanBuilder;

import java.time.LocalDateTime;

@Service
public class ProjectServiceImpl implements ProjectService {
	
    Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
	
    private final ProjectRepository projectRepo;
    private final Utils utils;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepo,
    		Utils utils) {
        this.projectRepo = projectRepo;
        this.utils = utils;
    }
    
    @Override
    public Page<ProjectConDTO> fetchAllProjects(Pageable page){
    	logger.info("fetch all projects service");
    	ModelMapper loose = utils.initModelMapperLoose();
        return projectRepo.findAll(page).map(project -> loose.map(project, ProjectConDTO.class));
    }
    
    @Override
    public ProjectConDTO getProjectById(Long id) {
    	logger.info("get project by id service");
    	ModelMapper loose = utils.initModelMapperLoose();
    	ProjectConDTO response = projectRepo.findById(id).map(project -> 
    		loose.map(project, ProjectConDTO.class)).orElseThrow(() -> 
    			new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));
    	return response;
    }
    
    @Override
    public ProjectConDTO getProjectByContractId(Long id) {
    	logger.info("get project by contract id service");
    	ModelMapper loose = utils.initModelMapperLoose();
    	return projectRepo.findByContracts_Id(id).map(project -> loose.map(project, ProjectConDTO.class)).orElseThrow(()->
    		new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND)
    	);
    }
    
    @Override
    public ProjectConDTO getProjectByAdam(String adam) {
    	logger.info("get project by adam service");
    	ModelMapper loose = utils.initModelMapperLoose();
    	return projectRepo.findByAdam(adam).map(project -> loose.map(project, ProjectConDTO.class)).orElseThrow(()->
    		new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND)
    	);
    };
    
    @Override
    public Page<ProjectConDTO> getProjectByCategory(ProjectsCategoryEnum category, Pageable page) {
    	logger.info("get project by category service");
    	ModelMapper loose = utils.initModelMapperLoose();
    	return projectRepo.findByProjectCategory(category, page).map(project -> loose.map(project, ProjectConDTO.class));
    }
    
    @Override
    public ProjectConDTO getProjectByProtocolNumber(String number) {
    	logger.info("get project by category service");
    	ModelMapper loose = utils.initModelMapperLoose();
    	return loose.map(projectRepo.findByProtocolNumber(number), ProjectConDTO.class);
    }
    
    @Override
    public Page<ProjectConDTO> getProjectByResponsibleEntity(String entity, Pageable page) {
    	logger.info("get project by category service");
    	ModelMapper loose = utils.initModelMapperLoose();
    	return projectRepo.findByResponsibleEntity(entity, page).map(project -> loose.map(project, ProjectConDTO.class));
    }
    
    @Override
    public Project addNewProject(CUDProjectDTO dto) {
//    	logger.info("add new project service");
    	ModelMapper strict = utils.initModelMapperStrict();
//    	ModelMapper loose = utils.initModelMapperLoose();
    	if(!validateProject(dto)) {
    		throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.VALUES_CANNOT_BE_NEGATIVE);
    	}
    	Project newProject = strict.map(dto, Project.class);
    	try {
            newProject.setDateCreated(LocalDateTime.now());
            //@TODO - PLACEHOLDER: CHANGE WITH USER PRINCIPAL
            newProject.setLastModifiedBy("dude");
    		return projectRepo.save(newProject);
//    		return loose.map(newProject, CUDProjectDTO.class);
    	} catch (Exception ex) {
    		if(ex.getCause() != null && 
    				ex.getCause().getCause() != null && 
    				ex.getCause().getCause().getMessage().contains("violates unique constraint")) {
    			if(ex.getCause().getCause().getMessage().contains("adam")) {
    				throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.ADAM_ALREADY_EXISTS);
				} else {
					throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.PROTOCOL_NUMBER_ALREADY_EXISTS);
				}
    		} else {
    			throw new MhteProjectsNotFoundException(ex.getMessage());
    		}
    	}
    }
    
    @Override
    public ProjectConDTO updateProject(CUDProjectDTO dto) {
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
        ProjectConDTO response = loose.map(projectExists, ProjectConDTO.class);
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
    public Page<ProjectConDTO> search(ProjectSearchDTO dto, Pageable pageable) {
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
                .map(project -> modelMapper.map(project, ProjectConDTO.class));
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
