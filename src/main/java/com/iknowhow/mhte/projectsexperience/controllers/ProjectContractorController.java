package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.projectsexperience.service.ProjectContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contractor")
public class ProjectContractorController {

    private final ProjectContractorService projectContractorService;

    @Autowired
    public ProjectContractorController(ProjectContractorService projectContractorService) {
        this.projectContractorService = projectContractorService;
    }
}
