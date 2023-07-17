package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Experience;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.dto.ExperienceDTO;

import java.util.List;

public interface ExperienceService {

    List<Experience> assignExperienceToProject(List<ExperienceDTO> dtoList,
                                               Project project,
                                               MhteUserPrincipal userPrincipal);

}
