package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectSubcontractor;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectSubcontractorRepository;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.UpdateProjectSubcontractorDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsAlreadyAssignedException;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


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
    public Page<?> searchProjectSubcontractors() {
        // @TODO -- PLACEHOLDER -- MICROSERVICE
        // @TODO -- QUERYDSL SEARCH WITH MEEP, Name, TaxId that fetches a DTO from another Microservice
        // @TODO -- Paginated DTOs return fields: MEEP, Name, TaxId, LegalType, Address, Series, DegreeValidUntil

        return null;
    }

    @Override
    public Page<ProjectSubcontractorResponseDTO> getAllSubcontractorsForProject(Long projectId, Pageable pageable) {
        // @TODO -- VARIOUS FIELDS INCLUDING name, taxId, type etc. must be filled from another MICROSERVICE
        projectRepository.findById(projectId).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));

        return subcontractorRepository.findAllByProjectId(projectId, pageable)
                .map(this::toSubcontractorResponseDTO);
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
    public void updateProjectSubcontractor(Long id, UpdateProjectSubcontractorDTO dto, MhteUserPrincipal userPrincipal) {
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

        // @TODO - PLACEHOLDER, CHANGE WITH PRINCIPAL USERNAME WHEN OKAY
        projectSubcontractor.setLastModifiedBy("OBELIX");
//        projectSubcontractor.setLastModifiedBy(userPrincipal.getUsername());

        subcontractorRepository.save(projectSubcontractor);

    }

    @Override
    @Transactional
    public void assignSubcontractorToProject(ProjectSubcontractorDTO dto, MhteUserPrincipal userPrincipal) {
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));

//        validateAlreadyAssignedSubcontractor(project.getProjectSubcontractors(), dto);

        ProjectSubcontractor subcontractor = new ProjectSubcontractor();
        subcontractor.setSubcontractorId(dto.getSubcontractorId());
        subcontractor.setProject(project);
        subcontractor.setContractValue(dto.getContractValue());
        subcontractor.setParticipationType(dto.getParticipationType());
        subcontractor.setContractDateFrom(dto.getContractDateFrom());
        subcontractor.setContractDateTo(dto.getContractDateTo());
        subcontractor.setContractGUID(dto.getContractGUID());

        subcontractor.setDateCreated(LocalDateTime.now());
        // @TODO - PLACEHOLDER, CHANGE WITH PRINCIPAL USERNAME WHEN OKAY
        subcontractor.setLastModifiedBy("ASTERIX");
//        subcontractor.setLastModifiedBy(userPrincipal.getUsername());

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

    private void validateAlreadyAssignedSubcontractor(
            List<ProjectSubcontractor> currentSubcontractors, ProjectSubcontractorDTO dto) {

        // check whether the current contractors contained the contractors to be assigned
        List<ProjectSubcontractor> subcontractorList = currentSubcontractors
                .stream()
                .filter(subcontractor -> Objects.equals(subcontractor.getSubcontractorId(), dto.getSubcontractorId()))
                .filter(subcontractor -> Objects.equals(subcontractor.getProject().getId(), dto.getProjectId()))
                .toList();

        if (!subcontractorList.isEmpty()) {
            throw new MhteProjectsAlreadyAssignedException(MhteProjectErrorMessage.ALREADY_ASSIGNED.name());
        }
    }
}
