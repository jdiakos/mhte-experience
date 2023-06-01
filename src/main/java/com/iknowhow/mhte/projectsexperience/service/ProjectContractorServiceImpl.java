package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectContractorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectContractorServiceImpl implements ProjectContractorService {

    private final ProjectContractorRepository projectContractorRepository;

    @Autowired
    public ProjectContractorServiceImpl(ProjectContractorRepository projectContractorRepository) {
        this.projectContractorRepository = projectContractorRepository;
    }
}
