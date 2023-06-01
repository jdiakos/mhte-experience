package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectSubcontractor;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectSubcontractorRepository;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.UpdateProjectSubcontractorDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectSubcontractorServiceImpl implements ProjectSubcontractorService {

    private final ProjectSubcontractorRepository subcontractorRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectSubcontractorServiceImpl(ProjectSubcontractorRepository subcontractorRepository,
                                           ProjectRepository projectRepository) {
        this.subcontractorRepository = subcontractorRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public List<ProjectSubcontractorResponseDTO> getAllSubcontractorsForProject(Long projectId) {
        // @TODO - PAGINATION
        projectRepository.findById(projectId).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));

        return subcontractorRepository.findAllByProjectId(projectId)
                .stream()
                .map(this::toSubcontractorResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectSubcontractorResponseDTO getSubcontractorOfProject(Long id) {
        ProjectSubcontractor projectSubcontractor = subcontractorRepository.findById(id).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_SUBCONTRACTOR_NOT_FOUND)
        );

        return toSubcontractorResponseDTO(projectSubcontractor);
    }

    @Override
    @Transactional
    public void updateProjectSubcontractor(Long id, UpdateProjectSubcontractorDTO dto) {
        ProjectSubcontractor projectSubcontractor = subcontractorRepository.findById(id).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_SUBCONTRACTOR_NOT_FOUND)
        );

        if (dto.getParticipationType() != null) {
            projectSubcontractor.setParticipationType(dto.getParticipationType());
        }
        if (dto.getContractValue() != null) {
            projectSubcontractor.setContractValue(dto.getContractValue());
        }
        if (dto.getContractDateFrom() != null) {
            projectSubcontractor.setContractDateFrom(dto.getContractDateFrom());
        }
        if (dto.getContractDateTo() != null) {
            projectSubcontractor.setContractDateTo(dto.getContractDateTo());
        }

        subcontractorRepository.save(projectSubcontractor);

    }

    @Override
    @Transactional
    public void assignSubcontractorToProject(ProjectSubcontractorDTO dto) {
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));

        ProjectSubcontractor subcontractor = new ProjectSubcontractor();
        // @TODO - CHECK IF ASSIGNED
        subcontractor.setSubcontractorId(dto.getSubcontractorId());
        subcontractor.setProject(project);
        subcontractor.setContractValue(dto.getContractValue());
        subcontractor.setParticipationType(dto.getParticipationType());
        subcontractor.setContractDateFrom(dto.getContractDateFrom());
        subcontractor.setContractDateTo(dto.getContractDateTo());
        subcontractor.setContractGUID(dto.getContractGUID());

        subcontractorRepository.save(subcontractor);

    }

    @Override
    @Transactional
    public void removeSubcontractorFromProject(Long id) {
        // @TODO - CHECK CONDITIONS FOR REMOVAL

        subcontractorRepository.deleteById(id);
    }

    private ProjectSubcontractorResponseDTO toSubcontractorResponseDTO(ProjectSubcontractor subcontractor) {
        ProjectSubcontractorResponseDTO dto = new ProjectSubcontractorResponseDTO();

        dto.setId(subcontractor.getId());
        dto.setSubcontractorId(subcontractor.getSubcontractorId());
        dto.setProjectId(subcontractor.getProject().getId());
        dto.setContractValue(subcontractor.getContractValue());
        dto.setParticipationType(subcontractor.getParticipationType());
        dto.setContractDateFrom(subcontractor.getContractDateFrom());
        dto.setContractDateTo(subcontractor.getContractDateTo());
        dto.setContractGUID(subcontractor.getContractGUID());

        return dto;
    }
}
