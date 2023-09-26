package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Experience;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceCategories;
import com.iknowhow.mhte.projectsexperience.domain.repository.ExperienceRepository;
import com.iknowhow.mhte.projectsexperience.dto.ExperienceDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.ExperienceResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.SearchExperienceByDTO;
import com.iknowhow.mhte.projectsexperience.feign.CompaniesFeignClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
                        experience.setCompanyTaxId(dto.getCompanyTaxId());
                        experience.setProject(project);
                        experience.setExperienceType(dto.getExperienceType());

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
    	List<ExperienceDTO> experienceDto = experiences.getContent().stream().map(this::toExperienceDTO).toList();
    	return new PageImpl<>(experienceDto, page, experiences.getTotalElements());
    }



    @Override
    public Page<ExperienceResponseDTO> getAllByStudyCategoryAndPersonTaxId(ExperienceCategories category,
                                                                   List<String> taxIds,
                                                                   Pageable pageable) {

        return experienceRepository.findAllByCategoryAndPersonTaxIdIn(category, taxIds, pageable)
                .map(this::toExperienceResponseDTO);
    }

    @Override
    public List<ExperienceResponseDTO> getAllByCompanyAndCategoryAndDateFrom(SearchExperienceByDTO dto) {
        return experienceRepository
                .findAllByCompanyTaxIdAndCategoryAndExperienceFromAfter(dto.getCompanyTaxId(), dto.getCategory(), dto.getDateFrom())
                .stream()
                .map(this::toExperienceResponseDTO)
                .toList();
    }

    @Override
    public List<ExperienceResponseDTO> getAllByCompanyTaxId(String companyTaxId) {
        return experienceRepository.findAllByCompanyTaxId(companyTaxId)
                .stream()
                .map(this::toExperienceResponseDTO)
                .toList();
    }

    @Override
    public Page<ExperienceDTO> getExperiencesByPerson(String taxId, Pageable pageable) {
        return experienceRepository.findAllByPersonTaxId(taxId, pageable)
                .map(this::toExperienceDTO);
    }

    private ExperienceDTO toExperienceDTO(Experience experience) {
        ExperienceDTO dto = new ExperienceDTO();
        dto.setId(experience.getId());
        dto.setPersonTaxId(experience.getPersonTaxId());
        dto.setExperienceFrom(experience.getExperienceFrom());
        dto.setExperienceTo(experience.getExperienceTo());
        dto.setCategory(experience.getCategory());
        dto.setOccupation(experience.getOccupation());
        dto.setRole(experience.getRole());
        dto.setValue(experience.getValue());

        return dto;
    }


    private ExperienceResponseDTO toExperienceResponseDTO(Experience experience) {
        ExperienceResponseDTO dto = new ExperienceResponseDTO();
        dto.setId(experience.getId());
        dto.setCategory(experience.getCategory());
        dto.setAdam(experience.getProject().getAdam());
        dto.setProtocolNumber(experience.getProject().getProtocolNumber());
        dto.setTitle(experience.getProject().getTitle());
        dto.setProjectEntity(experience.getProject().getResponsibleEntity());
        dto.setProjectValue(experience.getValue());
        dto.setFrom(experience.getExperienceFrom());
        dto.setTo(experience.getExperienceTo());

        long totalMonths = ChronoUnit.MONTHS.between(dto.getFrom(), dto.getTo());
        dto.setTotalMonths(totalMonths);

        return dto;
    }
}
