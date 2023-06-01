package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectContractor;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectContractorRepository;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectContractorServiceImpl implements ProjectContractorService {

    private final ProjectContractorRepository contractorRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectContractorServiceImpl(ProjectContractorRepository contractorRepository,
                                        ProjectRepository projectRepository) {
        this.contractorRepository = contractorRepository;
        this.projectRepository = projectRepository;
    }


    @Override
    public List<ProjectContractorResponseDTO> getAllContractorsForProject(Long projectId) {
        // Not sure if the results should be paginated, there wouldn't be that many contractors in a single project
        projectRepository.findById(projectId).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));


        return contractorRepository.findAllByProjectId(projectId)
                .stream()
                .map(this::toContractorResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assignContractorToProject(ProjectContractorDTO dto) {
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));

        ProjectContractor contractor = new ProjectContractor();
        contractor.setContractorId(dto.getContractorId());
        contractor.setProject(project);
        contractor.setParticipationType(dto.getParticipationType());
        contractor.setParticipationPercentage(dto.getParticipationPercentage());

        contractorRepository.save(contractor);

    }

    @Override
    public ProjectContractorResponseDTO getContractorOfProject(Long id) {
        ProjectContractor projectContractor = contractorRepository.findById(id).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_CONTRACTOR_NOT_FOUND)
        );

        return toContractorResponseDTO(projectContractor);
    }

    public void updateProjectContractor() {

    }

    @Override
    public void removeContractorFromProject(Long id) {
        // @TODO - CHECKS

        contractorRepository.deleteById(id);
    }


    private ProjectContractorResponseDTO toContractorResponseDTO(ProjectContractor contractor) {
        ProjectContractorResponseDTO dto = new ProjectContractorResponseDTO();

        dto.setId(contractor.getId());
        dto.setContractorId(contractor.getContractorId());
        dto.setProjectId(contractor.getProject().getId());
        dto.setParticipationType(contractor.getParticipationType());
        dto.setParticipationPercentage(contractor.getParticipationPercentage());

        return dto;
    }
}
