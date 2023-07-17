package com.iknowhow.mhte.projectsexperience.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectContractorDTO {

    private Long id;
    private Long contractorId;
    private String participationType;
    private Double participationPercentage;

}
