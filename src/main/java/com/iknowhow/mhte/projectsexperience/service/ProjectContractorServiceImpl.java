package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectContractor;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectContractorRepository;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.dto.ContractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ContractorResponseDTO;
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
    public List<ContractorResponseDTO> getAllContractorsForProject(Long projectId) {
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
    public void assignContractorToProject(ContractorDTO dto) {
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));

        ProjectContractor contractor = new ProjectContractor();
        contractor.setContractorId(dto.getContractorId());
        contractor.setProject(project);
        contractor.setParticipationType(dto.getParticipationType());
        contractor.setParticipationPercentage(dto.getParticipationPercentage());

        contractorRepository.save(contractor);

    }

    public void updateProjectContractor() {

    }

    public void removeContractorFromProject() {

    }


    private ContractorResponseDTO toContractorResponseDTO(ProjectContractor contractor) {
        ContractorResponseDTO dto = new ContractorResponseDTO();

        dto.setId(contractor.getId());
        dto.setContractorId(contractor.getContractorId());
        dto.setProjectId(contractor.getProject().getId());
        dto.setParticipationType(contractor.getParticipationType());
        dto.setParticipationPercentage(contractor.getParticipationPercentage());

        return dto;
    }
}
