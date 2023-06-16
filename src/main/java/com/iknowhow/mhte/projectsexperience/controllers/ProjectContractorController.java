package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.UpdateProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.service.ProjectContractorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{projectId}/get-all")
    public ResponseEntity<Page<ProjectContractorResponseDTO>> getAllContractorsByProject(@PathVariable Long projectId,
                                                                                         Pageable pageable) {
        Page<ProjectContractorResponseDTO> response =
                projectContractorService.getAllContractorsForProject(projectId, pageable);

        return ResponseEntity.ok().body(response);
    }

    // @TODO - REMOVE ON REFACTOR
    @PostMapping("/assign")
    public ResponseEntity<Void> assignContractorToProject(@Valid @RequestBody ProjectContractorDTO dto,
                                                          @AuthenticationPrincipal MhteUserPrincipal userPrincipal) {
        projectContractorService.assignContractorToProject(dto, userPrincipal);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectContractorResponseDTO> getContractorForProject(@PathVariable Long id) {
        ProjectContractorResponseDTO response = projectContractorService.getContractorOfProject(id);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateContractorOfProject(@PathVariable Long id,
                                                          @RequestBody UpdateProjectContractorDTO dto,
                                                          @AuthenticationPrincipal MhteUserPrincipal userPrincipal) {
        projectContractorService.updateProjectContractor(id, dto, userPrincipal);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeContractorFromProject(@PathVariable Long id) {
        projectContractorService.removeContractorFromProject(id);

        return ResponseEntity.ok().build();
    }

}
