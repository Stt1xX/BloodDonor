package com.bloodlink.dto.mapper;

import com.bloodlink.dto.request.BloodUnitCreateRequest;
import com.bloodlink.dto.response.BloodUnitResponse;
import com.bloodlink.entities.BloodUnit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for BloodUnit entity and DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BloodUnitMapper {

    /**
     * Converts create request DTO to BloodUnit entity.
     * Note: bloodBank must be set manually after mapping.
     */
    @Mapping(target = "bloodBank", ignore = true)
    @Mapping(source = "rhesusFactor", target = "rhFactor")
    BloodUnit toEntity(BloodUnitCreateRequest request);

    /**
     * Converts BloodUnit entity to response DTO.
     */
    @Mapping(source = "rhFactor", target = "rhesusFactor")
    BloodUnitResponse toResponse(BloodUnit entity);

    /**
     * Updates existing entity from request DTO.
     */
    @Mapping(target = "bloodBank", ignore = true)
    @Mapping(source = "rhesusFactor", target = "rhFactor")
    void updateEntityFromRequest(BloodUnitCreateRequest request, @MappingTarget BloodUnit entity);
}
