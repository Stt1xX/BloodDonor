package com.bloodlink.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RhFactor {
    POSITIVE("+"),
    NEGATIVE("-");

    private final String symbol;

    RhFactor(String symbol) {
        this.symbol = symbol;
    }

    @JsonValue
    public String getSymbol() {
        return symbol;
    }

    @JsonCreator
    public static RhFactor fromSymbol(String symbol) {
        for (RhFactor factor : values()) {
            if (factor.symbol.equals(symbol)) {
                return factor;
            }
        }
        return null;
    }
}
