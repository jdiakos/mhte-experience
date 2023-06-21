package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectContractor;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.UpdateProjectContractorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProjectContractorService {

    Page<?> searchProjectContractors();

    Page<ProjectContractorResponseDTO> getAllContractorsForProject(Long projectId, Pageable pageable);

    ProjectContractorResponseDTO getContractorOfProject(Long id);

    // @TODO - FOR REMOVAL
    void updateProjectContractor(Long id, UpdateProjectContractorDTO dto, MhteUserPrincipal userPrincipal);

//    List<ProjectContractor> assignContractorsToProject(List<ProjectContractorDTO> dtoList,
//                                    Project project,
//                                    MhteUserPrincipal userPrincipal);

    // @TODO - FOR REMOVAL
    void removeContractorFromProject(Long id);

	List<ProjectContractor> assignContractorsToProject(List<ProjectContractorDTO> dtoList, Project project,
			MhteUserPrincipal userPrincipal);

}
