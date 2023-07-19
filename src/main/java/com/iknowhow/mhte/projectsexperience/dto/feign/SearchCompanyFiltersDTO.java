package com.iknowhow.mhte.projectsexperience.dto.feign;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchCompanyFiltersDTO {

    // private String registryNumber;
    private String companyName;
    private String taxId;
    private String recordNumber;
    private String type;
    private String status;
    private LocalDate degreeExpirationDateFrom;
    private LocalDate degreeExpirationDateTo;

}
