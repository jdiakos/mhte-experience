package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorResponseDTO;

import java.util.List;

public interface ProjectContractorService {

    List<ProjectContractorResponseDTO> getAllContractorsForProject(Long projectId);

    ProjectContractorResponseDTO getContractorOfProject(Long id);

    void assignContractorToProject(ProjectContractorDTO dto);

    void removeContractorFromProject(Long id);

}
