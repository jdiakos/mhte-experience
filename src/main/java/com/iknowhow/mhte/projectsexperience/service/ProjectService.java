package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.dto.ProjectMasterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.iknowhow.mhte.projectsexperience.dto.CUDProjectDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSearchDTO;

public interface ProjectService {
	
	Page<ProjectResponseDTO> fetchAllProjects(Pageable page);
	
	ProjectResponseDTO getProjectById(Long id);
	
	void createProject(MhteUserPrincipal userPrincipal,
			ProjectMasterDTO dto, 
    		MultipartFile[] subcontractorFiles,
    		MultipartFile[] contractFiles,
    		MultipartFile[] documents);
		
	CUDProjectDTO deleteProject(Long id);
	
	Page<ProjectResponseDTO> search(ProjectSearchDTO dto, Pageable pageable);

	void updateProject(MhteUserPrincipal userPrincipal, ProjectMasterDTO dto, MultipartFile[] subcontractorFiles,
			MultipartFile[] contractFiles, MultipartFile[] documents);

}
