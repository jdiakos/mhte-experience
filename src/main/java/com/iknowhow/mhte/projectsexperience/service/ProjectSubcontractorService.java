package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectSubcontractor;
import com.iknowhow.mhte.projectsexperience.dto.ProjectDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.CompanyDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.SearchCompanyFiltersDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ProjectSubcontractorService {

    Page<CompanyDTO> searchProjectSubcontractors(SearchCompanyFiltersDTO dto,
												 Pageable pageable);

    List<ProjectSubcontractorDTO> getAllSubcontractorsForProject(Long projectId);

	List<ProjectSubcontractor> assignSubcontractorsToProject(List<ProjectSubcontractorDTO> dtoList,
			MultipartFile[] subcontractorFiles, Project project, MhteUserPrincipal userPrincipal);
	
	List<ProjectSubcontractor> objectsToBeDeleted(Project project, ProjectDTO dto);

}
