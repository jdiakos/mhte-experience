package com.iknowhow.mhte.projectsexperience.domain.enums;

public enum CompanyType {
    SOLE_PROPRIETORSHIP ("Sole Proprietorship"),
    GENERAL_PARTNERSHIP ("General Partnership (GP)"),
    LIMITED_PARTNERSHIP ("Limited Partnership (LLP)"),
    LIMITED_LIABILITY_COMPANY ("Limited Liability Company (LLC)"),
    SINGLE_MEMBER_LIMITED_LIABILITY_COMPANY ("Single Member Limited Liability Company (SMLLC)"),
    PRIVATE_COMPANY ("Private Company (PC)"),
    SINGLE_MEMBER_PRIVATE_COMPANY ("Single Member Private Company (SUP)"),
    SA ("Anonymi Etaireia (SA)"),
    SPECIAL_COOPERATIVE_ENTERPRISE ("Special Cooperative Enterprise")
    ;


    private final String label;

    CompanyType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
