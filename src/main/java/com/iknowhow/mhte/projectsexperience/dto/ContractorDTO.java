package com.iknowhow.mhte.projectsexperience.dto;

import lombok.Data;

@Data
public class ContractorDTO {

    private Long projectId;
    private Long contractorId;
    private String participationType;
    private Double participationPercentage;

}
