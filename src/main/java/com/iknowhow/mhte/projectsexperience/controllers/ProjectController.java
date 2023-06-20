package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.dto.ProjectMasterDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import com.iknowhow.mhte.projectsexperience.dto.CUDProjectDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSearchDTO;
import com.iknowhow.mhte.projectsexperience.service.ProjectService;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<Page<ProjectResponseDTO>> getProjects(Pageable page) {
    	Page<ProjectResponseDTO> projects = projectService.fetchAllProjects(page);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable(value="id") Long id) {
    	ProjectResponseDTO project = projectService.getProjectById(id);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }
    
    @GetMapping("/get-by-contract-id")
    public ResponseEntity<ProjectResponseDTO> getProjectByContractId(@RequestParam("id") Long id) {
    	ProjectResponseDTO project = projectService.getProjectByContractId(id);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }
    
    @GetMapping("/get-by-adam")
    public ResponseEntity<ProjectResponseDTO> getProjectByAdam(@RequestParam("adam") String id) {
    	ProjectResponseDTO project = projectService.getProjectByAdam(id);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }
    
    @GetMapping("/get-by-category")
    public ResponseEntity<Page<ProjectResponseDTO>> getProjectByCategory(@RequestParam("category") ProjectsCategoryEnum category,
                                                                         Pageable pageable) {
    	Page<ProjectResponseDTO> project = projectService.getProjectByCategory(category, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }
    
    @GetMapping("/get-by-protocol")
    public ResponseEntity<ProjectResponseDTO> getProjectByProtocolNumber(@RequestParam("protocolNumber") String protocolNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getProjectByProtocolNumber(protocolNumber));
    }
    
    @GetMapping("/get-by-entity")
    public ResponseEntity<Page<ProjectResponseDTO>> getProjectByResponsibleEntity(@RequestParam("entity") String entity,
                                                                                  Pageable pageable) {
    	Page<ProjectResponseDTO> project = projectService.getProjectByResponsibleEntity(entity, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    // @TODO -- DON'T FORGET TO ADD THE FILE UPLOADS!!!!
    @PostMapping("/create-project")
    public ResponseEntity<Void> createProject(@AuthenticationPrincipal MhteUserPrincipal userPrincipal,
                                              @Valid @RequestPart("project") ProjectMasterDTO dto,
                                              @RequestPart("contracts") MultipartFile[] contracts,
                                              @RequestPart("subcontractors") MultipartFile[] subcontractors,
                                              @RequestPart("documents") MultipartFile[] documents) {
        projectService.createProject(dto, userPrincipal);

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/update-project")
    public ResponseEntity<ProjectResponseDTO> updateProject(@RequestBody CUDProjectDTO project){
    	return ResponseEntity.status(HttpStatus.OK).body(projectService.updateProject(project));
    }
    
    @DeleteMapping(value = "/project/{id}")
    public ResponseEntity<CUDProjectDTO> deleteProject(@PathVariable(value="id") Long id){
    	return ResponseEntity.status(HttpStatus.OK).body(projectService.deleteProject(id));
    }
    
    @PostMapping("/search")
    public ResponseEntity<Page<ProjectResponseDTO>> searchForProjects(@RequestBody ProjectSearchDTO dto, Pageable pageable) {
        logger.info("Search for users");
        Page<ProjectResponseDTO> result = projectService.search(dto, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    //@TODO: remove this after testing
    @GetMapping("/test-principal")
    public ResponseEntity<MhteUserPrincipal> getProjects(@AuthenticationPrincipal MhteUserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(principal);
    }
}
