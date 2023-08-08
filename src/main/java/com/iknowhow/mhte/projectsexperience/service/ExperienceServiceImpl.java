package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Experience;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.repository.ExperienceRepository;
import com.iknowhow.mhte.projectsexperience.dto.ExperienceDTO;
import com.iknowhow.mhte.projectsexperience.feign.CompaniesFeignClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final CompaniesFeignClient companyClient;

    @Autowired
    public ExperienceServiceImpl(ExperienceRepository experienceRepository,
    		CompaniesFeignClient companyClient) {
        this.experienceRepository = experienceRepository;
        this.companyClient = companyClient;
    }

    @Override
    @Transactional
    public List<Experience> assignExperienceToProject(List<ExperienceDTO> dtoList,
                                                      Project project,
                                                      MhteUserPrincipal userPrincipal) {

        if (dtoList != null && !dtoList.isEmpty()) {
            return dtoList
                    .stream()
                    .map(dto -> {
                        Experience experience = new Experience();
                        if (dto.getId() != null) {
                            experience.setId(dto.getId());
                        }

                        Optional.ofNullable(dto.getPersonTaxId())
                                .ifPresent(id -> {
                                    experience.setPersonTaxId(dto.getPersonTaxId());
                                    experience.setOccupation(dto.getOccupation());
                                    experience.setRole(dto.getRole());
                                });
                        experience.setExperienceFrom(dto.getExperienceFrom());
                        experience.setExperienceTo(dto.getExperienceTo());
                        experience.setValue(dto.getValue());
                        experience.setCategory(dto.getCategory());
                        experience.setProject(project);

                        if (project.getId() != null) {
                            project.getExperiences().clear();
                        }

                        return experience;
                    })
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }
    
    @Override
    public Page<ExperienceDTO> getExperienceByCompanyId(Long companyId, Pageable page){
    	List<Long> experienceIds = companyClient.getExperienceIds(companyId);
    	Page<Experience> experiences = experienceRepository.findAllByIdIn(experienceIds, page);
    	List<ExperienceDTO> experienceDto = experiences.getContent().stream().map(e -> {
    		ExperienceDTO dto = new ExperienceDTO();
    		dto.setCategory(e.getCategory());
    		dto.setExperienceFrom(e.getExperienceFrom());
    		dto.setExperienceTo(e.getExperienceTo());
    		dto.setId(e.getId());
    		dto.setOccupation(e.getOccupation());
    		dto.setPersonTaxId(e.getPersonTaxId());
    		dto.setRole(e.getRole());
    		dto.setValue(e.getValue());
    		return dto;
    	}).toList();
    	return new PageImpl<>(experienceDto, page, experienceDto.size());
    }

}
