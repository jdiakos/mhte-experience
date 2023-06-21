package com.iknowhow.mhte.projectsexperience.dto;

import java.time.LocalDate;
import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import lombok.Data;

// @TODO - FOR REMOVAL
@Data
public class CUDProjectDTO {
	
    private ProjectsCategoryEnum projectCategory;
    private String adam;
    private String protocolNumber;
    private String title;
    private String description;
    private String responsibleEntity;
    private String contractingAuthority;
    private String headAuthority;
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
