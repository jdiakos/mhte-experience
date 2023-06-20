package com.iknowhow.mhte.projectsexperience.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectFinancialElementsDTO {

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
