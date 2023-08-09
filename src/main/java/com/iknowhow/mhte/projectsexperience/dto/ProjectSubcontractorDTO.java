package com.iknowhow.mhte.projectsexperience.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectSubcontractorDTO {

    private Long id;
    private Long subcontractorId;
    private String subcontractorName;
    private String participationType;
    private Double contractValue;
    private LocalDate contractDateFrom;
    private LocalDate contractDateTo;
    private String contractGUID;
    private String contractFilename;

}
