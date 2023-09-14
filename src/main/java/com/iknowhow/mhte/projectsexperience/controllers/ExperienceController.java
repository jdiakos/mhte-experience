package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceCategories;
import com.iknowhow.mhte.projectsexperience.dto.ExperienceDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.ExperienceResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.SearchExperienceByDTO;
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


    @PostMapping("/get-all/{category}")
    public ResponseEntity<Page<ExperienceResponseDTO>> getAllByCategoryAndPersonTaxId(@PathVariable("category") ExperienceCategories category,
                                                                              @RequestBody List<String> taxIds,
                                                                              Pageable pageable) {
        Page<ExperienceResponseDTO> response = experienceService.getAllByStudyCategoryAndPersonTaxId(category, taxIds, pageable);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-company-experience")
    public Page<ExperienceDTO> getExperienceByCompanyId(@RequestParam(name="companyId") Long companyId, Pageable page){
        return experienceService.getExperienceByCompanyId(companyId, page);
    }

    @PostMapping("/get-by-company-category-date-from")
    public ResponseEntity<List<ExperienceResponseDTO>> getExperiencesByCompanyAndCategoryAndDateFrom(@RequestBody SearchExperienceByDTO dto) {
        List<ExperienceResponseDTO> response = experienceService.getAllByCompanyAndCategoryAndDateFrom(dto);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-by-company-tax-id/{companyTaxId}")
    public ResponseEntity<List<ExperienceResponseDTO>> getExperiencesByCompanyTaxId(@PathVariable("companyTaxId") String companyTaxId) {
        List<ExperienceResponseDTO> response = experienceService.getAllByCompanyId(companyTaxId);

        return ResponseEntity.ok().body(response);
    }

}
