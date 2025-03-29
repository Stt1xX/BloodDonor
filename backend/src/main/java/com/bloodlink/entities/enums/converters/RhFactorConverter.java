package com.bloodlink.entities.enums.converters;

import com.bloodlink.entities.enums.RhFactor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RhFactorConverter implements AttributeConverter<RhFactor, String> {

    @Override
    public String convertToDatabaseColumn(RhFactor rhFactor) {
        return (rhFactor != null) ? rhFactor.getSymbol() : null;
    }

    @Override
    public RhFactor convertToEntityAttribute(String dbData) {
        return (dbData != null) ? RhFactor.fromSymbol(dbData) : null;
    }
}

