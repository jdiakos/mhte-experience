package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectSubcontractor;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.UpdateProjectSubcontractorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ProjectSubcontractorService {

    Page<?> searchProjectSubcontractors();

    Page<ProjectSubcontractorResponseDTO> getAllSubcontractorsForProject(Long projectId, Pageable pageable);

    ProjectSubcontractorResponseDTO getSubcontractorOfProject(Long id);

    void updateProjectSubcontractor(Long id, UpdateProjectSubcontractorDTO dto, MhteUserPrincipal userPrincipal);

    void removeSubcontractorFromProject(Long id);
    
	List<ProjectSubcontractor> assignSubcontractorsToProject(List<ProjectSubcontractorDTO> dtoList,
			MultipartFile[] subcontractorFiles, Project project, MhteUserPrincipal userPrincipal);

}
