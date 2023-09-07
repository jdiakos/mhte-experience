package com.iknowhow.mhte.projectsexperience.domain.enums;

public enum ExperienceRole {

    SUPERVISOR("Supervisor"),
    OPERATOR("Operator"),
    OVERSEER("Overseer"),
    ;

    final String label;

    ExperienceRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
