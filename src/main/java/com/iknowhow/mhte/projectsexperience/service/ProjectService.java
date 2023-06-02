package com.iknowhow.mhte.projectsexperience.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import com.iknowhow.mhte.projectsexperience.dto.CUDProjectDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectConDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSearchDTO;

public interface ProjectService {
	
	Page<ProjectConDTO> fetchAllProjects(Pageable page);
	
	ProjectConDTO getProjectById(Long id);
	
	ProjectConDTO getProjectByAdam(String adam);
	
	CUDProjectDTO addNewProject(CUDProjectDTO dto);
	
	ProjectConDTO updateProject(CUDProjectDTO dto);
	
	CUDProjectDTO deleteProject(Long id);
	
	ProjectConDTO getProjectByContractId(Long id);
	
	Page<ProjectConDTO> getProjectByCategory(ProjectsCategoryEnum category, Pageable page);
	
	ProjectConDTO getProjectByProtocolNumber(String protocolNumber);
	
	Page<ProjectConDTO> getProjectByResponsibleEntity(String entity, Pageable page);
	
	Page<ProjectConDTO> search(ProjectSearchDTO dto, Pageable pageable);

}
