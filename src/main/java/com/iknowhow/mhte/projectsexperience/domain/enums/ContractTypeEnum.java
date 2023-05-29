package com.iknowhow.mhte.projectsexperience.domain.enums;

public enum ContractTypeEnum {

    INITIAL_CONTRACT("INITIAL_CONTRACT", "Αρχική Σύμβαση"),
    ADDITIONAL_CONTRACT("ADDITIONAL_CONTRACT","Συμπληρωματική Σύμβαση"),
    MODIFYING_CONTRACT("MODIFYING_CONTRACT", "Τροποποιητική Σύμβαση");

    private final String key;
    private final String label;

    ContractTypeEnum(String key, String label) {
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
