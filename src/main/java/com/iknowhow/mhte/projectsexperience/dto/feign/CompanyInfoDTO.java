package com.iknowhow.mhte.projectsexperience.dto.feign;

import lombok.Data;

@Data
public class CompanyInfoDTO {

    private String companyName;
    private String companyAddress;
    private String companyType;
    private String companySeat;
    private String taxId;
    private String taxAuthority;
    private String companyEmail;
    private String companyPhone;

}
