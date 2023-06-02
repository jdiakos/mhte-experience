package com.iknowhow.mhte.projectsexperience.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.dto.CUDProjectDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectConDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectCustomValidationException;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import com.iknowhow.mhte.projectsexperience.utils.Utils;

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
    public CUDProjectDTO addNewProject(CUDProjectDTO dto) {
    	logger.info("add new project service");
    	ModelMapper strict = utils.initModelMapperStrict();
    	ModelMapper loose = utils.initModelMapperLoose();
    	if(!validateProject(dto)) {
    		throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.VALUES_CANNOT_BE_NEGATIVE);
    	}
    	Project newProject = strict.map(dto, Project.class);
    	try {
    		projectRepo.save(newProject);
    		return loose.map(newProject, CUDProjectDTO.class);
    	} catch (Exception ex) {
    		if(ex.getCause() != null && 
    				ex.getCause().getCause() != null && 
    				ex.getCause().getCause().getMessage().contains("violates unique constraint")) {
    			if(ex.getCause().getCause().getMessage().contains("adam")) {
    				throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.ADAM_ALREADY_EXISTS);
				} else {
					throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.PROTOCOL_NUMBER_ALREADY_EXISTS);
				}
    		}else {
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
        projectRepo.delete(projectExists);
        return response;
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
