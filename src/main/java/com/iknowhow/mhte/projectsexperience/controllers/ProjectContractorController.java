package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.projectsexperience.dto.ContractorDTO;
import com.iknowhow.mhte.projectsexperience.service.ProjectContractorService;
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
    public ResponseEntity<List<ContractorDTO>> getAllContractorsByProject(@PathVariable Long projectId) {
        List<ContractorDTO> response = projectContractorService.getAllContractorsForProject(projectId);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> assignContractorToProject(@RequestBody ContractorDTO dto) {
        projectContractorService.assignContractorToProject(dto);

        return ResponseEntity.ok().build();
    }

}
