package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.service.ProjectContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/contractor")
public class ProjectContractorController {

    private final ProjectContractorService projectContractorService;

    @Autowired
    public ProjectContractorController(ProjectContractorService projectContractorService) {
        this.projectContractorService = projectContractorService;
    }

    @PostMapping("/search-by-params")
    public ResponseEntity<?> searchContractorsByParams() {
        // @TODO -- PLACEHOLDER: PENDING MICROSERVICE TO IMPLEMENT
        Page<?> response = projectContractorService.searchProjectContractors();

        return null;
    }

    // @TODO - FOR REMOVAL
    @GetMapping("/{projectId}/get-all")
    public ResponseEntity<Page<ProjectContractorResponseDTO>> getAllContractorsByProject(@PathVariable Long projectId,
                                                                                         Pageable pageable) {
        Page<ProjectContractorResponseDTO> response =
                projectContractorService.getAllContractorsForProject(projectId, pageable);

        return ResponseEntity.ok().body(response);
    }

    // @TODO - FOR REMOVAL
    @GetMapping("/{id}")
    public ResponseEntity<ProjectContractorResponseDTO> getContractorForProject(@PathVariable Long id) {
        ProjectContractorResponseDTO response = projectContractorService.getContractorOfProject(id);

        return ResponseEntity.ok().body(response);
    }

}
