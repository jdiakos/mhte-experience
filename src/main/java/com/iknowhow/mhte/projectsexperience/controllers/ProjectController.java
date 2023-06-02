package com.iknowhow.mhte.projectsexperience.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.iknowhow.mhte.projectsexperience.dto.CUDProjectDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectConDTO;
import com.iknowhow.mhte.projectsexperience.service.ProjectService;
import com.iknowhow.mhte.projectsexperience.service.ProjectServiceImpl;

@RestController
@RequestMapping("/projects")
public class ProjectController {
	
    Logger logger = LoggerFactory.getLogger(ProjectController.class);
	
	private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    
    @GetMapping("/all")
    public ResponseEntity<Page<ProjectConDTO>> getProjects(Pageable page) {
    	Page<ProjectConDTO> projects = projectService.fetchAllProjects(page);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }
    
    @GetMapping("/project/{id}")
    public ResponseEntity<ProjectConDTO> getProjectById(@PathVariable(value="id") Long id) {
    	ProjectConDTO project = projectService.getProjectById(id);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }
    
    @GetMapping("/project-by-contract")
    public ResponseEntity<ProjectConDTO> getProjectByContrantId(@RequestParam("id") Long id) {
    	ProjectConDTO project = projectService.getProjectByContractId(id);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }
    
    @PostMapping(value = "/add-project")
    public ResponseEntity<CUDProjectDTO> addNewProject(@RequestBody CUDProjectDTO project){
    	return ResponseEntity.status(HttpStatus.OK).body(projectService.addNewProject(project));
    }
    
    @PutMapping(value = "/update-project")
    public ResponseEntity<ProjectConDTO> updateProject(@RequestBody CUDProjectDTO project){
    	return ResponseEntity.status(HttpStatus.OK).body(projectService.updateProject(project));
    }
    
    @DeleteMapping(value = "/project/{id}")
    public ResponseEntity<CUDProjectDTO> updateProject(@PathVariable(value="id") Long id){
    	return ResponseEntity.status(HttpStatus.OK).body(projectService.deleteProject(id));
    } 

}
