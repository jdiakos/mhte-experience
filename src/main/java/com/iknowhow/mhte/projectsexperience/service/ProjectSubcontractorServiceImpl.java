package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectSubcontractor;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectSubcontractorRepository;
import com.iknowhow.mhte.projectsexperience.dto.ProjectMasterDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


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
    public List<ProjectSubcontractorDTO> getAllSubcontractorsForProject(Long projectId) {
        // @TODO -- VARIOUS FIELDS INCLUDING name, taxId, type etc. must be filled from another MICROSERVICE
        projectRepository.findById(projectId).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));

        return subcontractorRepository.findAllByProjectId(projectId)
                .stream()
                .map(this::toSubcontractorDTO)
                .toList();
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
            // @TODO - PLACEHOLDER: CHANGE WITH USER PRINCIPAL
            subcontractor.setLastModifiedBy("ASTERIX");
//          contract.setLastModifiedBy(userPrincipal.getUsername());
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

    private ProjectSubcontractorDTO toSubcontractorDTO(ProjectSubcontractor subcontractor) {
        ProjectSubcontractorDTO dto = new ProjectSubcontractorDTO();

        dto.setId(subcontractor.getId());
        dto.setSubcontractorId(subcontractor.getSubcontractorId());
        dto.setContractValue(subcontractor.getContractValue());
        dto.setParticipationType(subcontractor.getParticipationType());
        dto.setContractDateFrom(subcontractor.getContractDateFrom());
        dto.setContractDateTo(subcontractor.getContractDateTo());
        dto.setContractGUID(subcontractor.getContractGUID());

        return dto;
    }

    @Override
    public List<ProjectSubcontractor> objectsToBeDeleted(Project project,  ProjectMasterDTO dto) {
    	List<ProjectSubcontractor> subcontractors = new ArrayList<>();
    	List<Long> oldIds = project.getProjectSubcontractors().
        		stream().
        		map(ProjectSubcontractor::getId).toList();
        List<Long> newIds = dto.getProjectSubcontractors().
        		stream().
        		filter(s -> s.getId() != null).
        		map(ProjectSubcontractorDTO::getId).toList();
        List<Long> differences = oldIds.stream()
                .filter(element -> !newIds.contains(element))
                .collect(Collectors.toList());
        for(int i=0; i<project.getProjectSubcontractors().size(); i++) {
        	if(differences.contains(project.getProjectSubcontractors().get(i).getId())) {
        		subcontractors.add(project.getProjectSubcontractors().get(i));
        		subcontractors.get(subcontractors.size()-1).setProject(null);
        	}
        }
        return subcontractors;
    }


}
