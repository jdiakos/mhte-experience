package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectContractor;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectContractorRepository;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.UpdateProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


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
    public void assignContractorToProject(ProjectContractorDTO dto, MhteUserPrincipal userPrincipal) {
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));

        validateAlreadyAssignedContractor(project.getProjectContractors(), dto);
        validateProjectParticipationPercentages(project, dto.getParticipationPercentage());

        ProjectContractor contractor = new ProjectContractor();
        contractor.setContractorId(dto.getContractorId());
        contractor.setProject(project);
        contractor.setParticipationType(dto.getParticipationType());
        contractor.setParticipationPercentage(dto.getParticipationPercentage());

        contractor.setDateCreated(LocalDateTime.now());
        // @TODO - PLACEHOLDER, CHANGE WITH PRINCIPAL USERNAME WHEN OKAY
        contractor.setLastModifiedBy("ASTERIX");
//        contractor.setLastModifiedBy(userPrincipal.getUsername());

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
    public void updateProjectContractor(Long id, UpdateProjectContractorDTO dto, MhteUserPrincipal userPrincipal) {
        ProjectContractor projectContractor = contractorRepository.findById(id).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_CONTRACTOR_NOT_FOUND)
        );

        validateProjectParticipationPercentages(projectContractor.getProject(), dto.getParticipationPercentage());

        if (dto.getParticipationType() != null) {
            projectContractor.setParticipationType(dto.getParticipationType());
        }
        if (dto.getParticipationPercentage() != null) {
            projectContractor.setParticipationPercentage(dto.getParticipationPercentage());
        }
        // @TODO - PLACEHOLDER, CHANGE WITH PRINCIPAL USERNAME WHEN OKAY
        projectContractor.setLastModifiedBy("OBELIX");
//        projectContractor.setLastModifiedBy(userPrincipal.getUsername());

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

    private void validateAlreadyAssignedContractor(
            List<ProjectContractor> currentContractors, ProjectContractorDTO dto) {

        // check whether the current contractors contained the contractors to be assigned
        List<ProjectContractor> contractorList = currentContractors
                .stream()
                .filter(contractor -> Objects.equals(contractor.getContractorId(), dto.getContractorId()))
                .filter(contractor -> Objects.equals(contractor.getProject().getId(), dto.getProjectId()))
                .toList();

        if (!contractorList.isEmpty()) {
            throw new MhteProjectsAlreadyAssignedException(MhteProjectErrorMessage.ALREADY_ASSIGNED.name());
        }

    }

    private void validateProjectParticipationPercentages(Project project, Double contractorShare) {
        // no one contractor's share in a project can exceed 100%
        if (contractorShare > 100) {
            throw new MhteProjectCustomValidationException(
                    MhteProjectErrorMessage.TOTAL_PERCENTAGE_EXCEEDS_MAX.name()
            );
        }

        // for any contractor updates or new additions, we check whether the new total percentage
        // exceeds 100%. If there is a change in percentages already reaching 100%,
        // the user must first lower one of the existing ones before increasing the other one
        // not the best solution UX-wise, but the only one foolproof I can think of right now
        double currentPercentage = project.getProjectContractors()
                .stream()
                .mapToDouble(ProjectContractor::getParticipationPercentage)
                .sum();

        if ((currentPercentage + contractorShare) > 100 && project.getProjectContractors().size() != 1) {
            throw new MhteProjectCustomValidationException(
                    MhteProjectErrorMessage.TOTAL_PERCENTAGE_EXCEEDS_MAX.name()
            );
        }

    }
}
