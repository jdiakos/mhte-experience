package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.UpdateProjectSubcontractorDTO;
import com.iknowhow.mhte.projectsexperience.service.ProjectSubcontractorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subcontractor")
public class ProjectSubcontractorController {

    private final ProjectSubcontractorService projectSubcontractorService;

    @Autowired
    public ProjectSubcontractorController(ProjectSubcontractorService projectSubcontractorService) {
        this.projectSubcontractorService = projectSubcontractorService;
    }

    @GetMapping("/{projectId}/get-all")
    public ResponseEntity<List<ProjectSubcontractorResponseDTO>> getAllSubcontractorsByProject(
            @PathVariable Long projectId) {

        List<ProjectSubcontractorResponseDTO> response =
                projectSubcontractorService.getAllSubcontractorsForProject(projectId);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> assignSubcontractorToProject(@Valid @RequestBody ProjectSubcontractorDTO dto) {
        projectSubcontractorService.assignSubcontractorToProject(dto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectSubcontractorResponseDTO> getSubcontractorForProject(@PathVariable Long id) {
        ProjectSubcontractorResponseDTO response = projectSubcontractorService.getSubcontractorOfProject(id);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSubcontractorOfProject(@PathVariable Long id,
                                                             @RequestBody UpdateProjectSubcontractorDTO dto) {
        projectSubcontractorService.updateProjectSubcontractor(id, dto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeSubcontractorFromProject(@PathVariable Long id) {
        projectSubcontractorService.removeSubcontractorFromProject(id);

        return ResponseEntity.ok().build();
    }

}
