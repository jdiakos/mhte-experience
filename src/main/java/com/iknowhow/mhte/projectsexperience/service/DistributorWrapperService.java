package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.dto.ProjectMasterDTO;

public interface DistributorWrapperService {

    void createProject(ProjectMasterDTO dto, MhteUserPrincipal userPrincipal);
}
