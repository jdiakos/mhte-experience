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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;


@Service
public class ProjectSubcontractorServiceImpl implements ProjectSubcontractorService {

    private final ProjectSubcontractorRepository subcontractorRepository;
    private final ProjectRepository projectRepository;
    private final FileNetService fileNetService;

    @Autowired
    public ProjectSubcontractorServiceImpl(ProjectSubcontractorRepository subcontractorRepository,
                                           ProjectRepository projectRepository,
                                           FileNetService fileNetService) {
    	this.fileNetService = fileNetService;
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

        Optional.ofNullable(dto.getParticipationType()).ifPresent(projectSubcontractor::setParticipationType);
        Optional.ofNullable(dto.getContractValue()).ifPresent(projectSubcontractor::setContractValue);
        Optional.ofNullable(dto.getContractDateFrom()).ifPresent(projectSubcontractor::setContractDateFrom);
        Optional.ofNullable(dto.getContractDateTo()).ifPresent(projectSubcontractor::setContractDateTo);

        // @TODO - PLACEHOLDER, CHANGE WITH PRINCIPAL USERNAME WHEN OKAY
        projectSubcontractor.setLastModifiedBy("OBELIX");
//        projectSubcontractor.setLastModifiedBy(userPrincipal.getUsername());

        subcontractorRepository.save(projectSubcontractor);

    }

    @Override
    @Transactional
    public List<ProjectSubcontractor> assignSubcontractorsToProject(List<ProjectSubcontractorDTO> dtoList,
                                                                    MultipartFile[] subcontractorFiles,
                                                                    Project project,
                                                                    MhteUserPrincipal userPrincipal) {
    	
    	List<ProjectSubcontractor> subcontractors = new ArrayList<>();
    	for (int i=0; i<dtoList.size(); i++) {
    		ProjectSubcontractor subcontractor = new ProjectSubcontractor();
    		if(dtoList.get(i).getId()!=null) {
    			subcontractor.setId(dtoList.get(i).getId());
            }
            subcontractor.setSubcontractorId(dtoList.get(i).getSubcontractorId());
            subcontractor.setProject(project);
            subcontractor.setContractValue(dtoList.get(i).getContractValue());
            subcontractor.setParticipationType(dtoList.get(i).getParticipationType());
            subcontractor.setContractDateFrom(dtoList.get(i).getContractDateFrom());
            subcontractor.setContractDateTo(dtoList.get(i).getContractDateTo());
            subcontractor.setDateCreated(LocalDateTime.now());
            subcontractor.setLastModifiedBy("ASTERIX");
            if (dtoList.get(i).getContractGUID() != null) {
            	subcontractor.setContractGUID(dtoList.get(i).getContractGUID());
                subcontractor.setContractFilename(subcontractorFiles[i].getOriginalFilename());
            } else {
            	subcontractor.setContractGUID(
                        fileNetService.uploadFileToFilenet(project, subcontractorFiles[i], "ASTERIX")
                );
                subcontractor.setContractFilename(subcontractorFiles[i].getOriginalFilename());
            }
            subcontractors.add(subcontractor);
    	}
        return subcontractors;
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
//        dto.setContractGUID(subcontractor.getContractGUID());

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
