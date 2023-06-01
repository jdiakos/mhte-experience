package com.iknowhow.mhte.projectsexperience.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;

@Service
public class ProjectServiceImpl implements ProjectService {
	
    private final ProjectRepository projectRepo;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepo) {
        this.projectRepo = projectRepo;
    }
    
    @Override
    public Project getProjectById(Long id) {
    	return projectRepo.findById(id).orElseThrow(() -> 
    		new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));
    }

}
