package com.iknowhow.mhte.projectsexperience.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectContractorDTO {

    @NotNull
    private Long projectId;
    @NotNull
    private Long contractorId;
    @NotBlank
    private String participationType;
    @NotNull
    private Double participationPercentage;

}
