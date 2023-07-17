package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.projectsexperience.dto.feign.CompanyInfoResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.SearchCompanyInfoDTO;
import com.iknowhow.mhte.projectsexperience.service.ProjectSubcontractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Page<CompanyInfoResponseDTO>> searchSubcontractorByParams(@RequestBody SearchCompanyInfoDTO dto,
                                                                                    Pageable pageable) {
        // @TODO -- PLACEHOLDER: PENDING MICROSERVICE TO IMPLEMENT
        Page<CompanyInfoResponseDTO> response = projectSubcontractorService.searchProjectSubcontractors(dto, pageable);

        return ResponseEntity.ok().body(response);
    }


}
