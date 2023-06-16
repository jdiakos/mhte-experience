package com.iknowhow.mhte.projectsexperience.dto;

import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectMasterDTO {

    // GENERAL DATA
    private ProjectsCategoryEnum projectCategory;
    private String adam;
    private String protocolNumber;
    private String title;
    private String description;
    private String responsibleEntity;
    private String contractingAuthority;
    private String headAuthority;

    // FINANCIAL DATA
    private Double initialContractBudget;
    private Double initialContractValue;
    private Double supplementaryContractValue;
    private Double apeValue;
    private Double totalValue;
    private LocalDate dateOfSigning;
    private LocalDate estimatedCompletionDate;
    private LocalDate completionDate;
    private LocalDate receiptProtocolDate;
    private String receiptProtocolNumber;
    private String type;

    // DEPENDANT ENTITIES
    private List<ContractDTO> contracts;
    private List<ProjectContractorDTO> projectContractors;
    private List<ProjectSubcontractorDTO> projectSubcontractors;

}
