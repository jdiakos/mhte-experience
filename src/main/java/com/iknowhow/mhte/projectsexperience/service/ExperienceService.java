package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Experience;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.enums.StudyCategories;
import com.iknowhow.mhte.projectsexperience.dto.ExperienceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

public interface ExperienceService {

    List<Experience> assignExperienceToProject(List<ExperienceDTO> dtoList,
                                               Project project,
                                               MhteUserPrincipal userPrincipal);
    
    Page<ExperienceDTO> getExperienceByCompanyId(Long companyId, Pageable page);

    Page<ExperienceDTO> getAllByStudyCategoryAndPersonTaxId(StudyCategories category,
                                                            String personTaxId,
                                                            Pageable pageable);

}
