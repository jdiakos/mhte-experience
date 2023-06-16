package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.dto.ProjectMasterDTO;
import com.iknowhow.mhte.projectsexperience.service.DistributorWrapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import com.iknowhow.mhte.projectsexperience.dto.CUDProjectDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectConDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSearchDTO;
import com.iknowhow.mhte.projectsexperience.service.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {
	
    Logger logger = LoggerFactory.getLogger(ProjectController.class);
	
	private final ProjectService projectService;
    private final DistributorWrapperService distributorWrapperService;

    @Autowired
    public ProjectController(ProjectService projectService,
                             DistributorWrapperService distributorWrapperService) {
        this.projectService = projectService;
        this.distributorWrapperService = distributorWrapperService;
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
    
    @GetMapping("/get-by-contract-id")
    public ResponseEntity<ProjectConDTO> getProjectByContractId(@RequestParam("id") Long id) {
    	ProjectConDTO project = projectService.getProjectByContractId(id);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }
    
    @GetMapping("/get-by-adam")
    public ResponseEntity<ProjectConDTO> getProjectByAdam(@RequestParam("adam") String id) {
    	ProjectConDTO project = projectService.getProjectByAdam(id);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }
    
    @GetMapping("/get-by-category")
    public ResponseEntity<Page<ProjectConDTO>> getProjectByCategory(@RequestParam("category") ProjectsCategoryEnum category,
    		Pageable pageable) {
    	Page<ProjectConDTO> project = projectService.getProjectByCategory(category, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }
    
    @GetMapping("/get-by-protocol")
    public ResponseEntity<ProjectConDTO> getProjectByProtocolNumber(@RequestParam("protocolNumber") String protocolNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getProjectByProtocolNumber(protocolNumber));
    }
    
    @GetMapping("/get-by-entity")
    public ResponseEntity<Page<ProjectConDTO>> getProjectByResponsibleEntity(@RequestParam("entity") String entity,
    		Pageable pageable) {
    	Page<ProjectConDTO> project = projectService.getProjectByResponsibleEntity(entity, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    // @TODO -- DON'T FORGET TO ADD THE FILE UPLOADS!!!!
    @PostMapping("/create-project")
    public ResponseEntity<Void> createProject(@RequestBody ProjectMasterDTO dto) {
        distributorWrapperService.createProject(dto);

        return ResponseEntity.ok().build();
    }

    // @TODO - REMOVE ON REFACTOR
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
    
    @PostMapping("/search")
    public ResponseEntity<Page<ProjectConDTO>> searchForProjects(@RequestBody ProjectSearchDTO dto, Pageable pageable) {
        logger.info("Search for users");
        Page<ProjectConDTO> result = projectService.search(dto, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    //@TODO: remove this after testing
    @GetMapping("/test-principal")
    public ResponseEntity<MhteUserPrincipal> getProjects(@AuthenticationPrincipal MhteUserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(principal);
    }
}
