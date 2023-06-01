package com.iknowhow.mhte.projectsexperience.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectSubcontractorDTO {

    @NotNull
    private Long projectId;
    @NotNull
    private Long subcontractorId;
    @NotBlank
    private String participationType;
    @NotNull
    private Double contractValue;
    @NotNull
    private LocalDate contractDateFrom;
    @NotNull
    private LocalDate contractDateTo;
    @NotBlank
    private String contractGUID;

}
