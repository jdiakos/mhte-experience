package com.iknowhow.mhte.projectsexperience.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectSubcontractorDTO {

    private Long projectId;
//    @NotNull
    private Long subcontractorId;
//    @NotBlank
    private String participationType;
//    @NotNull
    private Double contractValue;
//    @NotNull
//    @Temporal(TemporalType.DATE)
    private LocalDate contractDateFrom;
//    @NotNull
//    @Temporal(TemporalType.DATE)
    private LocalDate contractDateTo;
//    @NotBlank
//    private String contractGUID;

}
