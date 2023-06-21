package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.dto.DownloadFileDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectMasterDTO;
import com.iknowhow.mhte.projectsexperience.service.FileNetService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.iknowhow.mhte.projectsexperience.dto.ProjectResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSearchDTO;
import com.iknowhow.mhte.projectsexperience.service.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {
	
    Logger logger = LoggerFactory.getLogger(ProjectController.class);
	
	private final ProjectService projectService;
    private final FileNetService fileNetService;

    @Autowired
    public ProjectController(ProjectService projectService,
                             FileNetService fileNetService) {
        this.projectService = projectService;
        this.fileNetService = fileNetService;
    }
    
    @GetMapping("/get-all")
    public ResponseEntity<Page<ProjectResponseDTO>> getProjects(Pageable page) {
    	Page<ProjectResponseDTO> projects = projectService.fetchAllProjects(page);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @PostMapping(value= "/create-project")
    public ResponseEntity<Void> createProject(@AuthenticationPrincipal MhteUserPrincipal userPrincipal, 
    		@Valid  @RequestPart ProjectMasterDTO dto,
    		@RequestPart("subcontractorFiles") MultipartFile[] subcontractorFiles,
    		@RequestPart("contractFiles") MultipartFile[] contractFiles,
    		@RequestPart("documents") MultipartFile[] documents) {
        //@TODO - CHANGE WITH PRINCIPAL USERNAME WHEN OKAY
    	projectService.createProject(userPrincipal, dto, subcontractorFiles, contractFiles, documents);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping(value= "/update-project")
    public ResponseEntity<Void> updateProject(@AuthenticationPrincipal MhteUserPrincipal userPrincipal, 
    		@Valid  @RequestPart ProjectMasterDTO dto,
    		@RequestPart("subcontractorFiles") MultipartFile[] subcontractorFiles,
    		@RequestPart("contractFiles") MultipartFile[] contractFiles,
    		@RequestPart("documents") MultipartFile[] documents) {
    	projectService.updateProject(userPrincipal, dto, subcontractorFiles, contractFiles, documents);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping(value = "/remove/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable(value="id") Long id){
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/search")
    public ResponseEntity<Page<ProjectResponseDTO>> searchForProjects(@RequestBody ProjectSearchDTO dto, Pageable pageable) {
        logger.info("Search for users");
        Page<ProjectResponseDTO> result = projectService.search(dto, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/download-file/{guid}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("guid") String guid) {
        logger.info("Downloading file");
        DownloadFileDTO dto = fileNetService.fetchByGuid(guid);

        ContentDisposition contentDisposition = ContentDisposition
                .builder("attachment")
                .filename(dto.getFilename())
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(contentDisposition);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dto.getFile());
    }

}
