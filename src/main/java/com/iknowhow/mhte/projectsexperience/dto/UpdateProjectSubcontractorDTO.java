package com.iknowhow.mhte.projectsexperience.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProjectSubcontractorDTO {

    private String participationType;
    private Double contractValue;
    private LocalDate contractDateFrom;
    private LocalDate contractDateTo;

}
