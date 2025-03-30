package com.bloodlink.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RequestStatus {
    PENDING("В ожидании"),
    REJECTED("Отклонено"),
    DELETED("Удалено"),
    COMPLETED("Принято");

    final String name;

    RequestStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static RequestStatus fromName(String name) {
        for (RequestStatus status : values()) {
            if (status.name.equals(name)) {
                return status;
            }
        }
        return null;
    }
}