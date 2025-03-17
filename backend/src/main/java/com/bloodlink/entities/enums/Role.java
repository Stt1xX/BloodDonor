package com.bloodlink.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum Role {
    ADMIN("admin"),
    MEDICAL_EMPLOYEE("medical_worker"),
    BLOOD_BANK_EMPLOYEE("bank_employee");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @JsonCreator
    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.getName().equalsIgnoreCase(role)) {
                return r;
            }
        }
        return null;
    }
}
