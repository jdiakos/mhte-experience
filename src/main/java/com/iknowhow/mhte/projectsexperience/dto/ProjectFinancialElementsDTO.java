package com.iknowhow.mhte.projectsexperience.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectFinancialElementsDTO {

    @NotNull
    private Double initialContractBudget;
    @NotNull
    private Double initialContractValue;
    private Double supplementaryContractValue;
    private Double apeValue;
    private Double totalValue;
    @NotNull
    private LocalDate dateOfSigning;
    @NotNull
    private LocalDate estimatedCompletionDate;
    private LocalDate completionDate;
    private LocalDate receiptProtocolDate;
    private String receiptProtocolNumber;
    private String type;

}
