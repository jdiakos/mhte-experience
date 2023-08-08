package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.projectsexperience.domain.enums.StudyCategories;
import com.iknowhow.mhte.projectsexperience.dto.ExperienceDTO;
import com.iknowhow.mhte.projectsexperience.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/experience")
public class ExperienceController {

    private final ExperienceService experienceService;

    @Autowired
    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }


    @GetMapping("/get-all/{category}/{personTaxId}")
    public ResponseEntity<Page<ExperienceDTO>> getAllByCategoryAndPersonTaxId(@PathVariable("personTaxId") String taxId,
                                                                              @PathVariable("category") StudyCategories category,
                                                                              Pageable pageable) {
        Page<ExperienceDTO> response = experienceService.getAllByStudyCategoryAndPersonTaxId(category, taxId, pageable);

        return ResponseEntity.ok().body(response);
    }
}
