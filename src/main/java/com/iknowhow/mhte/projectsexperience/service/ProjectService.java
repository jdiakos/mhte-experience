package com.iknowhow.mhte.projectsexperience.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.iknowhow.mhte.projectsexperience.dto.CUDProjectDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectConDTO;

public interface ProjectService {
	
	Page<ProjectConDTO> fetchAllProjects(Pageable page);
	
	ProjectConDTO getProjectById(Long id);
	
	CUDProjectDTO addNewProject(CUDProjectDTO dto);
	
	ProjectConDTO updateProject(CUDProjectDTO dto);
	
	CUDProjectDTO deleteProject(Long id);
	
	ProjectConDTO getProjectByContractId(Long id);

}
