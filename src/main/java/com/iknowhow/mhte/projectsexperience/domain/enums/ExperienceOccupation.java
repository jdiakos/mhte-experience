package com.iknowhow.mhte.projectsexperience.domain.enums;

public enum ExperienceOccupation {

	OCCUPATION_DEFAULT_SUPERVISION("Default_Supervision"),
	OCCUPATION_DEFAULT_OPERATION("Default_Operation"),
	OCCUPATION_DEFAULT_OVERSEER("Default_Overseer"),
	STUDY("Study"),
	OCCUPATION_CONSTRUCTION("Construction"),
	OCCUPATION_FOREMAN("Foreman")
    ;

    final String label;

    ExperienceOccupation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
