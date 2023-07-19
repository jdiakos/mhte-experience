package com.iknowhow.mhte.projectsexperience.domain.enums;

public enum ExperienceOccupation {

    SUPERVISION("Supervision"),
    ;

    final String label;

    ExperienceOccupation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
