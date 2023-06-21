package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.UpdateProjectSubcontractorDTO;
import com.iknowhow.mhte.projectsexperience.service.ProjectSubcontractorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/subcontractor")
public class ProjectSubcontractorController {

    private final ProjectSubcontractorService projectSubcontractorService;

    @Autowired
    public ProjectSubcontractorController(ProjectSubcontractorService projectSubcontractorService) {
        this.projectSubcontractorService = projectSubcontractorService;
    }

    @PostMapping("/search-by-params")
    public ResponseEntity<?> searchSubcontractorByParams() {
        // @TODO -- PLACEHOLDER: PENDING MICROSERVICE TO IMPLEMENT
        Page<?> response = projectSubcontractorService.searchProjectSubcontractors();

        return null;
    }

    @GetMapping("/{projectId}/get-all")
    public ResponseEntity<Page<ProjectSubcontractorResponseDTO>> getAllSubcontractorsByProject(
            @PathVariable Long projectId, Pageable pageable) {

        Page<ProjectSubcontractorResponseDTO> response =
                projectSubcontractorService.getAllSubcontractorsForProject(projectId, pageable);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectSubcontractorResponseDTO> getSubcontractorForProject(@PathVariable Long id) {
        ProjectSubcontractorResponseDTO response = projectSubcontractorService.getSubcontractorOfProject(id);

        return ResponseEntity.ok().body(response);
    }

    // @TODO - FOR REMOVAL
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSubcontractorOfProject(@PathVariable Long id,
                                                             @RequestBody UpdateProjectSubcontractorDTO dto,
                                                             @AuthenticationPrincipal MhteUserPrincipal userPrincipal) {
        projectSubcontractorService.updateProjectSubcontractor(id, dto, userPrincipal);

        return ResponseEntity.ok().build();
    }

    // @TODO - FOR REMOVAL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeSubcontractorFromProject(@PathVariable Long id) {
        projectSubcontractorService.removeSubcontractorFromProject(id);

        return ResponseEntity.ok().build();
    }

}
