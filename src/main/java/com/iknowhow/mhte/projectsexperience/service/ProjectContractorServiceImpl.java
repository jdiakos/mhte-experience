package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectContractor;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectContractorRepository;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.UpdateProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsAlreadyAssignedException;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    public Page<?> searchProjectContractors() {
        // @TODO -- PLACEHOLDER -- MICROSERVICE
        // @TODO -- QUERYDSL SEARCH WITH MEEP, Name, TaxId that fetches a DTO from another Microservice
        // @TODO -- Paginated DTOs return fields: MEEP, Name, TaxId, LegalType, Address, Series, DegreeValidUntil

        return null;
    }


    @Override
    public Page<ProjectContractorResponseDTO> getAllContractorsForProject(Long projectId, Pageable pageable) {
        // @TODO -- VARIOUS FIELDS INCLUDING name, taxId, type etc. must be filled from another MICROSERVICE
        projectRepository.findById(projectId).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));

        return contractorRepository.findAllByProjectId(pageable, projectId)
                .map(this::toContractorResponseDTO);
    }

    @Override
    @Transactional
    public void assignContractorToProject(ProjectContractorDTO dto) {
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));

        contractorRepository.findByContractorIdAndProjectId(dto.getContractorId(), dto.getProjectId())
                .ifPresent(contractor -> {
                    throw new MhteProjectsAlreadyAssignedException(MhteProjectErrorMessage.ALREADY_ASSIGNED.name());
                });

        ProjectContractor contractor = new ProjectContractor();
        contractor.setContractorId(dto.getContractorId());
        contractor.setProject(project);
        contractor.setParticipationType(dto.getParticipationType());
        // @TODO -- MUST NOT BE OVER 100%
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

    @Override
    @Transactional
    public void updateProjectContractor(Long id, UpdateProjectContractorDTO dto) {
        ProjectContractor projectContractor = contractorRepository.findById(id).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_CONTRACTOR_NOT_FOUND)
        );

        if (dto.getParticipationType() != null) {
            projectContractor.setParticipationType(dto.getParticipationType());
        }

        // @TODO -- TOTAL PERCENTAGE IN PROJECT MUST NOT EXCEED 100%
        if (dto.getParticipationPercentage() != null) {
            projectContractor.setParticipationPercentage(dto.getParticipationPercentage());
        }

        contractorRepository.save(projectContractor);
    }

    @Override
    @Transactional
    public void removeContractorFromProject(Long id) {
        // @TODO - CHECK CONDITIONS FOR REMOVAL

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
