package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.UpdateProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.service.ProjectContractorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/{projectId}/get-all")
    public ResponseEntity<List<ProjectContractorResponseDTO>> getAllContractorsByProject(@PathVariable Long projectId) {
        List<ProjectContractorResponseDTO> response = projectContractorService.getAllContractorsForProject(projectId);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> assignContractorToProject(@Valid @RequestBody ProjectContractorDTO dto) {
        projectContractorService.assignContractorToProject(dto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectContractorResponseDTO> getContractorForProject(@PathVariable Long id) {
        ProjectContractorResponseDTO response = projectContractorService.getContractorOfProject(id);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateContractorOfProject(@PathVariable Long id,
                                                          @RequestBody UpdateProjectContractorDTO dto) {
        projectContractorService.updateProjectContractor(id, dto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeContractorFromProject(@PathVariable Long id) {
        projectContractorService.removeContractorFromProject(id);

        return ResponseEntity.ok().build();
    }

}
