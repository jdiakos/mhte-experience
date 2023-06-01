package com.iknowhow.mhte.projectsexperience.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;

import com.iknowhow.mhte.projectsexperience.service.ProjectService;

@RestController
@RequestMapping("/project")
public class ProjectController {
	
	private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    
    @GetMapping("/project/{id}")
    public ResponseEntity<Project> getUsers(@PathVariable(value="id") Long id) {
    	Project project = projectService.getProjectById(id);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

}
