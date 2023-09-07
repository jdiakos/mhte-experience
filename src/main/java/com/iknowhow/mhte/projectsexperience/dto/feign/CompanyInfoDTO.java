package com.iknowhow.mhte.projectsexperience.dto.feign;

import com.iknowhow.mhte.projectsexperience.domain.enums.CompanyType;

import lombok.Data;

@Data
public class CompanyInfoDTO {

    private Long companyId;
    private String companyName;
    private String companyAddress;
    private CompanyType companyType;
    private String companySeat;
    private String taxId;
    private String taxAuthority;
    private String companyEmail;
    private String companyPhone;
    private String legalRepresentativeId;

}
