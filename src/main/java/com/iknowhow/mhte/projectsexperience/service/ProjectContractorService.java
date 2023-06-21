package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectContractor;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorDTO;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ProjectContractorService {

    Page<?> searchProjectContractors();

    List<ProjectContractorDTO> getAllContractorsForProject(Long projectId);

	List<ProjectContractor> assignContractorsToProject(List<ProjectContractorDTO> dtoList, Project project,
			MhteUserPrincipal userPrincipal);

//	void dischargeContractors(Project project, ProjectMasterDTO dto);

}
