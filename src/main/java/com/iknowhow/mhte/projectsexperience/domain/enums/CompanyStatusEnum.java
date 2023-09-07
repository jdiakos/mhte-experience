package com.iknowhow.mhte.projectsexperience.domain.enums;

public enum CompanyStatusEnum {

    ACTIVE_COMPANY("ACTIVE_COMPANY", "Σε ισχύ"),
    INACTIVE_COMPANY("INACTIVE_COMPANY", "Ανενεργή"),
    DELETED_COMPANY("DELETED_COMPANY", "Διαγραφείσα")
    ;

    private final String key;
    private final String label;

    CompanyStatusEnum(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getKey() {
        return key;
    }
}
