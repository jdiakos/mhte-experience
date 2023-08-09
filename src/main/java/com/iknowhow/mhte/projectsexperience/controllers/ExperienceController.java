package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.projectsexperience.domain.enums.StudyCategories;
import com.iknowhow.mhte.projectsexperience.dto.ExperienceDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.ExperienceResponseDTO;
import com.iknowhow.mhte.projectsexperience.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experience")
public class ExperienceController {

    private final ExperienceService experienceService;

    @Autowired
    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }


    @GetMapping("/get-all/{category}")
    public ResponseEntity<Page<ExperienceResponseDTO>> getAllByCategoryAndPersonTaxId(@PathVariable("category") StudyCategories category,
                                                                              @RequestBody List<String> taxIds,
                                                                              Pageable pageable) {
        Page<ExperienceResponseDTO> response = experienceService.getAllByStudyCategoryAndPersonTaxId(category, taxIds, pageable);

        return ResponseEntity.ok().body(response);
    }
}
