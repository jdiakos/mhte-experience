package com.iknowhow.mhte.projectsexperience.domain.enums;

public enum ProjectsCategoryEnum {

    PUBLIC_PROJECT("PUBLIC_PROJECT", "Δημόσιο Έργο"),
    PRIVATE_PROJECT("PRIVATE_PROJECT","Ιδιωτικό Έργο"),
    PUBLIC_STUDY("PUBLIC_STUDY", "Δημόσια Μελέτη"),
    PRIVATE_STUDY("PRIVATE_STUDY", "Ιδιωτική Μελέτη"),
    PUBLIC_SERVICE("PUBLIC_SERVICE", "Δημόσια Υπηρεσία"),
    PRIVATE_SERVICE("PRIVATE_SERVICE", "Ιδιωτική Υπηρεσία"),
    GRANT_PROJECT("GRANT_PROJECT", "Έργο Παραχώρησης");

    private final String key;
    private final String label;

    ProjectsCategoryEnum(String key, String label) {
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
