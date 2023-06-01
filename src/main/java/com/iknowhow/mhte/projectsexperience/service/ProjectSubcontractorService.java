package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.UpdateProjectSubcontractorDTO;

import java.util.List;

public interface ProjectSubcontractorService {

    List<ProjectSubcontractorResponseDTO> getAllSubcontractorsForProject(Long projectId);

    ProjectSubcontractorResponseDTO getSubcontractorOfProject(Long id);

    void updateProjectSubcontractor(Long id, UpdateProjectSubcontractorDTO dto);

    void assignSubcontractorToProject(ProjectSubcontractorDTO dto);

    void removeSubcontractorFromProject(Long id);

}
