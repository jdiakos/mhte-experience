package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.UpdateProjectContractorDTO;

import java.util.List;

public interface ProjectContractorService {

    List<ProjectContractorResponseDTO> getAllContractorsForProject(Long projectId);

    ProjectContractorResponseDTO getContractorOfProject(Long id);

    void updateProjectContractor(Long id, UpdateProjectContractorDTO dto);

    void assignContractorToProject(ProjectContractorDTO dto);

    void removeContractorFromProject(Long id);

}
