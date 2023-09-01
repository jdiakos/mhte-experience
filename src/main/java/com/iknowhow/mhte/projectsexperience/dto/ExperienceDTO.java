package com.iknowhow.mhte.projectsexperience.dto;

import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceOccupation;
import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceRole;
import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceCategories;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExperienceDTO {

    private Long id;
    private String personTaxId;
    private LocalDate experienceFrom;
    private LocalDate experienceTo;
    private ExperienceCategories category;
    private ExperienceOccupation occupation;
    private ExperienceRole role;
    private Double value;

}
