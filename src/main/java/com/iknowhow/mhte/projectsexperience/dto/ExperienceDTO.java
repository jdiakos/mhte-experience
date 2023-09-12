package com.iknowhow.mhte.projectsexperience.dto;

import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceOccupation;
import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceRole;
import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceType;
import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceCategories;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExperienceDTO {

    private Long id;
    private String personTaxId;
    private String companyTaxId;
    private LocalDate experienceFrom;
    private LocalDate experienceTo;
    private ExperienceCategories category;
    private ExperienceOccupation occupation;
    private ExperienceRole role;
    private ExperienceType experienceType;
    private Double value;

}
