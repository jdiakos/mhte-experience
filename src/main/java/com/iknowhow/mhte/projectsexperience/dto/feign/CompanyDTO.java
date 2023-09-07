package com.iknowhow.mhte.projectsexperience.dto.feign;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

import com.iknowhow.mhte.companies.dto.FinancialDataDTO;
import com.iknowhow.mhte.projectsexperience.domain.enums.CompanyStatusEnum;

@Data
public class CompanyDTO {

    private Long id;
    private String protocolNumber;
    private CompanyInfoDTO companyInfo;
    private Boolean recommended;
    private CompanyStatusEnum status;
    private LocalDate degreeStartDate;
    private LocalDate degreeEndDate;
    private Boolean isContractingPublicProjects;
    private Boolean isContractingPrivateProjects;
    private Boolean isStudyingPublicProjects;
    private Boolean isStudyingPrivateProjects;
    private Boolean isConsultant;
    private List<FinancialDataDTO> financialData;
    private List<CompanyContactPersonDTO> companyContactPersons;
}
