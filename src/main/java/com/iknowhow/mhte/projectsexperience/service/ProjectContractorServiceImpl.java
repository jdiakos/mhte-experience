package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectContractor;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectContractorRepository;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


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
    public List<ProjectContractorDTO> getAllContractorsForProject(Long projectId) {
        // @TODO -- VARIOUS FIELDS INCLUDING name, taxId, type etc. must be filled from another MICROSERVICE
        projectRepository.findById(projectId).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));

        return contractorRepository.findAllByProjectId(projectId)
                .stream()
                .map(this::toContractorDTO)
                .toList();
    }

    @Override
    @Transactional
    public List<ProjectContractor> assignContractorsToProject(List<ProjectContractorDTO> dtoList,
                                           Project project,
                                           MhteUserPrincipal userPrincipal) {

        List<ProjectContractor> contractors = dtoList
                .stream()
                .map(dto -> {
                    ProjectContractor contractor = new ProjectContractor();
                    if(dto.getId()==null) {
                        contractor = new ProjectContractor();
                    } else {
                        contractor.setId(dto.getId());
                    }
                    contractor.setContractorId(dto.getContractorId());
                    contractor.setProject(project);
                    contractor.setParticipationType(dto.getParticipationType());
                    contractor.setParticipationPercentage(dto.getParticipationPercentage());
                    contractor.setDateCreated(LocalDateTime.now());
                    // @TODO - PLACEHOLDER: CHANGE WITH USER PRINCIPAL
                    contractor.setLastModifiedBy("ASTERIX");
//                    contractor.setLastModifiedBy(userPrincipal.getUsername());
                    return contractor;
                })
                .toList();
        return contractors;

    }

    private ProjectContractorDTO toContractorDTO(ProjectContractor contractor) {
        ProjectContractorDTO dto = new ProjectContractorDTO();

        dto.setId(contractor.getId());
        dto.setContractorId(contractor.getContractorId());
        dto.setParticipationType(contractor.getParticipationType());
        dto.setParticipationPercentage(contractor.getParticipationPercentage());

        return dto;
    }

}
