package com.bloodlink.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RhFactor {
    POSITIVE("POSITIVE"),
    NEGATIVE("NEGATIVE");

    private final String value;

    RhFactor(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RhFactor fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (RhFactor factor : values()) {
            if (factor.value.equalsIgnoreCase(value)) {
                return factor;
            }
        }
        // Support legacy +/- symbols
        if ("+".equals(value)) return POSITIVE;
        if ("-".equals(value)) return NEGATIVE;
        throw new IllegalArgumentException("Unknown Rh factor: " + value);
    }

    // Display symbol for UI
    public String getDisplaySymbol() {
        return this == POSITIVE ? "+" : "-";
    }
}
