package com.bloodlink.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BloodGroup {
    A("A"),
    B("B"),
    AB("AB"),
    O("O");

    private final String value;

    BloodGroup(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static BloodGroup fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (BloodGroup type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown blood group: " + value);
    }
} 