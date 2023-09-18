package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.dto.AuditHistoryDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.iknowhow.mhte.projectsexperience.dto.ProjectSearchDTO;

import java.util.List;

public interface ProjectService {
	
	Page<ProjectDTO> fetchAllProjects(Pageable page);
	
	void createProject(MhteUserPrincipal userPrincipal,
					   ProjectDTO dto,
					   MultipartFile[] subcontractorFiles,
					   MultipartFile[] contractFiles,
					   MultipartFile[] documents);
		
	void deleteProject(Long id, MhteUserPrincipal userPrincipal);
	
	Page<ProjectDTO> search(ProjectSearchDTO dto, Pageable pageable);

	ProjectDTO getProjectById(Long projectId);

	void updateProject(MhteUserPrincipal userPrincipal, ProjectDTO dto, MultipartFile[] subcontractorFiles,
					   MultipartFile[] contractFiles, MultipartFile[] documents);

	List<AuditHistoryDTO> getProjectAuditHistory(Long projectId);

//	ProjectDTO getProjectAuditByRevisionNumber(Integer revisionNumber);
	ProjectDTO getProjectAuditByRevisionNumber(Integer revisionNumber, Long projectId);

}
