package com.bloodlink.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BloodGroup {
    A("1"),
    B("2"),
    AB("3"),
    O("4");

    private final String symbol;

    BloodGroup(String symbol) {
        this.symbol = symbol;
    }

    @JsonValue
    public String getSymbol() {
        return symbol;
    }

    @JsonCreator
    public static BloodGroup fromSymbol(String symbol) {
        for (BloodGroup type : values()) {
            if (type.symbol.equals(symbol)) {
                return type;
            }
        }
        return null;
    }
} 