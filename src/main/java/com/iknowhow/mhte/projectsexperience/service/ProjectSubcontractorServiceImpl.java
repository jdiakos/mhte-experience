package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectSubcontractor;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectSubcontractorRepository;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorResponseDTO;
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
    public List<ProjectSubcontractorResponseDTO> getAllSubcontractorsForProject(Long projectId) {
        // @TODO -- VARIOUS FIELDS INCLUDING name, taxId, type etc. must be filled from another MICROSERVICE
        projectRepository.findById(projectId).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));

        return subcontractorRepository.findAllByProjectId(projectId)
                .stream()
                .map(this::toSubcontractorResponseDTO)
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

    private ProjectSubcontractorResponseDTO toSubcontractorResponseDTO(ProjectSubcontractor subcontractor) {
        ProjectSubcontractorResponseDTO dto = new ProjectSubcontractorResponseDTO();

        dto.setId(subcontractor.getId());
        dto.setSubcontractorId(subcontractor.getSubcontractorId());
        dto.setContractValue(subcontractor.getContractValue());
        dto.setParticipationType(subcontractor.getParticipationType());
        dto.setContractDateFrom(subcontractor.getContractDateFrom());
        dto.setContractDateTo(subcontractor.getContractDateTo());
//        dto.setContractGUID(subcontractor.getContractGUID());

        return dto;
    }


}
