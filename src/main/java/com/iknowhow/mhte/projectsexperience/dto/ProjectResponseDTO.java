package com.iknowhow.mhte.projectsexperience.dto;

import java.time.LocalDate;
import java.util.List;
import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import lombok.Data;

@Data
public class ProjectResponseDTO {
	
	private Long id;
    private ProjectsCategoryEnum projectCategory;
    private String adam;
    private String protocolNumber;
    private String title;
    private String description;
    private String responsibleEntity;
    private String contractingAuthority;
    private String headAuthority;
    private List<ProjectContractorDTO> projectContractors;
    private List<ProjectSubcontractorDTO> projectSubcontractors;
    private List<ContractDTO> contracts;
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

}
