package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Experience;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceCategories;
import com.iknowhow.mhte.projectsexperience.dto.ExperienceDTO;

import java.time.LocalDate;
import java.util.List;

import com.iknowhow.mhte.projectsexperience.dto.feign.ExperienceResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.SearchExperienceByDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExperienceService {

    List<Experience> assignExperienceToProject(List<ExperienceDTO> dtoList,
                                               Project project,
                                               MhteUserPrincipal userPrincipal);
    
    Page<ExperienceDTO> getExperienceByCompanyId(Long companyId, Pageable page);

    Page<ExperienceResponseDTO> getAllByStudyCategoryAndPersonTaxId(ExperienceCategories category,
                                                                    List<String> taxIds,
                                                                    Pageable pageable);

    List<ExperienceResponseDTO> getAllByCompanyAndCategoryAndDateFrom(SearchExperienceByDTO dto);

    List<ExperienceResponseDTO> getAllByCompanyId(String companyTaxId);

}
