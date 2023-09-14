package com.iknowhow.mhte.projectsexperience.dto.feign;

import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceCategories;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchExperienceByDTO {

    private String companyTaxId;
    private ExperienceCategories category;
    private LocalDate dateFrom;

}
