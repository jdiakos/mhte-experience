package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectSubcontractor;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectSubcontractorRepository;
import com.iknowhow.mhte.projectsexperience.dto.ProjectDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectSubcontractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.CompanyInfoResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.SearchCompanyInfoDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import com.iknowhow.mhte.projectsexperience.feign.CompaniesFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final CompaniesFeignClient companiesFeignClient;

    @Autowired
    public ProjectSubcontractorServiceImpl(ProjectSubcontractorRepository subcontractorRepository,
                                           ProjectRepository projectRepository,
                                           FileNetService fileNetService,
                                           CompaniesFeignClient companiesFeignClient) {
    	this.fileNetService = fileNetService;
        this.subcontractorRepository = subcontractorRepository;
        this.projectRepository = projectRepository;
        this.companiesFeignClient = companiesFeignClient;
    }


    @Override
    public Page<CompanyInfoResponseDTO> searchProjectSubcontractors(SearchCompanyInfoDTO dto,
                                                                    Pageable pageable) {
        // @TODO -- MEEP, Grade, DegreeValidTo to be added

        return companiesFeignClient.searchCompanyInfo(dto, pageable);
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
    	int fileIndex=0;
    	List<ProjectSubcontractor> subcontractors = new ArrayList<>();
    	for (int i=0; i<dtoList.size(); i++) {
    		ProjectSubcontractor subcontractor = new ProjectSubcontractor();
    		if(dtoList.get(i).getId()!=null) {
        		List<Long> ids = project.getProjectSubcontractors().stream().map(ProjectSubcontractor::getId).toList();
    			if(!ids.contains(dtoList.get(i).getId())) {
    				throw new MhteProjectsNotFoundException(MhteProjectErrorMessage.SUBCONTRACTOR_ALREADY_ASSIGNED);
    			}
    			subcontractor.setId(dtoList.get(i).getId());
            }
            subcontractor.setSubcontractorId(dtoList.get(i).getSubcontractorId());
            subcontractor.setProject(project);
            subcontractor.setContractValue(dtoList.get(i).getContractValue());
            subcontractor.setParticipationType(dtoList.get(i).getParticipationType());
            subcontractor.setContractDateFrom(dtoList.get(i).getContractDateFrom());
            subcontractor.setContractDateTo(dtoList.get(i).getContractDateTo());
            subcontractor.setDateCreated(LocalDateTime.now());
            subcontractor.setLastModifiedBy(userPrincipal.getUsername());
            if (dtoList.get(i).getContractGUID() != null) {
            	subcontractor.setContractGUID(dtoList.get(i).getContractGUID());
                subcontractor.setContractFilename(dtoList.get(i).getContractFilename());
            } else {
            	if(subcontractorFiles==null || subcontractorFiles.length<=fileIndex) {
            		throw new MhteProjectsNotFoundException(MhteProjectErrorMessage.MISSING_FILE);
            	}
            	subcontractor.setContractGUID(
                        fileNetService.uploadFileToFilenet(project, subcontractorFiles[fileIndex], userPrincipal.getUsername())
                );
                subcontractor.setContractFilename(subcontractorFiles[fileIndex].getOriginalFilename());
                fileIndex++;
            }
            subcontractors.add(subcontractor);
    	}
    	if(project.getId()!=null) {
            project.getProjectSubcontractors().clear();
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
    public List<ProjectSubcontractor> objectsToBeDeleted(Project project,  ProjectDTO dto) {
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
