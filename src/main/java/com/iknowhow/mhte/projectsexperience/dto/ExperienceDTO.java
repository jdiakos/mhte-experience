package com.iknowhow.mhte.projectsexperience.dto;

import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceRole;
import com.iknowhow.mhte.projectsexperience.domain.enums.StudyCategories;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExperienceDTO {

    private Long id;
    private Long companyId;
    private Long personId;
    private LocalDate experienceFrom;
    private LocalDate experienceTo;
    private StudyCategories category;
    private ExperienceRole role;
    private Double value;


}
