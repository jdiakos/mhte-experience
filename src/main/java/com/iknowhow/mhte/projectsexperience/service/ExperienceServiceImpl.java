package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Experience;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.repository.ExperienceRepository;
import com.iknowhow.mhte.projectsexperience.dto.ExperienceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;

    @Autowired
    public ExperienceServiceImpl(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    @Override
    @Transactional
    public List<Experience> assignExperienceToProject(List<ExperienceDTO> dtoList,
                                                      Project project,
                                                      MhteUserPrincipal userPrincipal) {
        return dtoList
                .stream()
                .map(dto -> {
                    Experience experience = new Experience();
                    if (dto.getId() != null) {
                        experience.setId(dto.getId());
                    }

                    Optional.ofNullable(dto.getCompanyId())
                                    .ifPresent(id -> experience.setCompanyId(dto.getCompanyId()));
                    Optional.ofNullable(dto.getPersonId())
                                    .ifPresent(id -> {
                                        experience.setPersonId(dto.getPersonId());
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
    }
}
