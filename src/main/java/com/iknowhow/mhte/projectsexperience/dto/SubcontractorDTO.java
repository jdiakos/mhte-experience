package com.iknowhow.mhte.projectsexperience.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SubcontractorDTO {

    private Long projectId;
    private Long subcontractorId;
    private String participationType;
    private Double contractValue;
    private LocalDate contractDateFrom;
    private LocalDate contractDateTo;
    private String contractGUID;

}
