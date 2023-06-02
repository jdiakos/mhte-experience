package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.UpdateProjectContractorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProjectContractorService {

    Page<?> searchProjectContractors();

    Page<ProjectContractorResponseDTO> getAllContractorsForProject(Long projectId, Pageable pageable);

    ProjectContractorResponseDTO getContractorOfProject(Long id);

    void updateProjectContractor(Long id, UpdateProjectContractorDTO dto);

    void assignContractorToProject(ProjectContractorDTO dto);

    void removeContractorFromProject(Long id);

}
