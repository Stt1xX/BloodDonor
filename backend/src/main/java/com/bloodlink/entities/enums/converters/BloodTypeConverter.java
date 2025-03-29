package com.bloodlink.entities.enums.converters;

import com.bloodlink.entities.enums.BloodGroup;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BloodTypeConverter implements AttributeConverter<BloodGroup, String> {

    @Override
    public String convertToDatabaseColumn(BloodGroup bloodGroup) {
        return (bloodGroup != null) ? bloodGroup.getSymbol() : null;
    }

    @Override
    public BloodGroup convertToEntityAttribute(String dbData) {
        return (dbData != null) ? BloodGroup.fromSymbol(dbData) : null;
    }
}
