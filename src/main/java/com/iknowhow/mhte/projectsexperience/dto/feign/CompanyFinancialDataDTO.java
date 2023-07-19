package com.iknowhow.mhte.projectsexperience.dto.feign;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompanyFinancialDataDTO {

    private Double operatingCycle;
    private Double personalFunds;
    private Double totalFixed;
    private String financialStatusType;
    private LocalDate submissionDurationFrom;
    private LocalDate submissionDurationTo;

}
