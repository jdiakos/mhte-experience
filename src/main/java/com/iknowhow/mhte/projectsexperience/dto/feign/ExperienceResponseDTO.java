package com.iknowhow.mhte.projectsexperience.dto.feign;

import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceCategories;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExperienceResponseDTO {

    private Long id;
    private ExperienceCategories category;
    private String adam;
    private String protocolNumber;
    private String title;
    private String projectEntity;
    private Double projectValue;
    private String projectActivity;
    private LocalDate from;
    private LocalDate to;
    private Long totalMonths;

}
