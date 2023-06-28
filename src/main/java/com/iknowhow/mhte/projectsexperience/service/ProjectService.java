package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.dto.ProjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.iknowhow.mhte.projectsexperience.dto.ProjectSearchDTO;

public interface ProjectService {
	
	Page<ProjectDTO> fetchAllProjects(Pageable page);
	
	void createProject(MhteUserPrincipal userPrincipal,
					   ProjectDTO dto,
					   MultipartFile[] subcontractorFiles,
					   MultipartFile[] contractFiles,
					   MultipartFile[] documents);
		
	void deleteProject(Long id, MhteUserPrincipal userPrincipal);
	
	Page<ProjectDTO> search(ProjectSearchDTO dto, Pageable pageable);

	void updateProject(MhteUserPrincipal userPrincipal, ProjectDTO dto, MultipartFile[] subcontractorFiles,
					   MultipartFile[] contractFiles, MultipartFile[] documents);

}
