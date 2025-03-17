package com.bloodlink.entities.enums;

public enum OrganizationType {
    BLOOD_BANK("blood_bank"),
    MEDICAL_INSTITUTION("medical_institution");

    OrganizationType(String medicalInstitution) {

    }

    public static OrganizationType fromString(String type) {
        for (OrganizationType organizationType : OrganizationType.values()) {
            if (organizationType.toString().equalsIgnoreCase(type)) {
                return organizationType;
            }
        }
        return null;
    }
}
