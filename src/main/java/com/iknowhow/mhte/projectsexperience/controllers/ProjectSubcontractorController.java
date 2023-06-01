package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.projectsexperience.service.ProjectSubcontractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subcontractor")
public class ProjectSubcontractorController {

    private final ProjectSubcontractorService projectSubcontractorService;

    @Autowired
    public ProjectSubcontractorController(ProjectSubcontractorService projectSubcontractorService) {
        this.projectSubcontractorService = projectSubcontractorService;
    }

    // enter contractor (anathesi, axia symvasis, date from, date to, contract file)

}
