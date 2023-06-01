package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectSubcontractorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectSubcontractorServiceImpl implements ProjectSubcontractorService {

    private final ProjectSubcontractorRepository projectSubcontractorRepository;

    @Autowired
    public ProjectSubcontractorServiceImpl(ProjectSubcontractorRepository projectSubcontractorRepository) {
        this.projectSubcontractorRepository = projectSubcontractorRepository;
    }
}
