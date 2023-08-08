package com.iknowhow.mhte.projectsexperience.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.iknowhow.mhte.projectsexperience.service.ExperienceService;
import com.iknowhow.mhte.projectsexperience.dto.*;

@RestController
@RequestMapping("/experience")
public class ProjectExperienceController {
	
	private final ExperienceService experienceService;
	
	public ProjectExperienceController(ExperienceService experienceService) {
		this.experienceService = experienceService;
	}
	
	@GetMapping("/get-company-experience")
	public Page<ExperienceDTO> getExperienceByCompanyId(@RequestParam(name="companyId") Long companyId, Pageable page){
		return experienceService.getExperienceByCompanyId(companyId, page);
	}

}
