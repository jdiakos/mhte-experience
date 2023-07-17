package com.iknowhow.mhte.projectsexperience.dto.feign;

import lombok.Data;

@Data
public class CompanyInfoResponseDTO {

    private Long id;
    private String companyName;
    private String companyType;
    private String taxId;
    private String companyAddress;
    // private String companyGrade;
    // private LocalDate degreeEndsIn;

}
