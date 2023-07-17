package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.projectsexperience.dto.feign.CompanyInfoResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.SearchCompanyInfoDTO;
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
    public ResponseEntity<Page<CompanyInfoResponseDTO>> searchContractorsByParams(@RequestBody SearchCompanyInfoDTO dto,
                                                                                  Pageable pageable) {

        Page<CompanyInfoResponseDTO> response = projectContractorService.searchProjectContractors(dto, pageable);

        return ResponseEntity.ok().body(response);
    }

}
