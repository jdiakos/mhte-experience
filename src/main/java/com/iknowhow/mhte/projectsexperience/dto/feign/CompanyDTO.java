package com.iknowhow.mhte.projectsexperience.dto.feign;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompanyDTO {

    private Long id;
    private String protocolNumber;
    private CompanyInfoDTO companyInfo;
    private Boolean recommended;
    private String status;
    private LocalDate degreeStartDate;
    private LocalDate degreeEndDate;
    private Boolean isContractingPublicProjects;
    private Boolean isContractingPrivateProjects;
    private Boolean isStudyingPublicProjects;
    private Boolean isStudyingPrivateProjects;
    private Boolean isConsultant;
    private CompanyFinancialDataDTO financialData;
}
