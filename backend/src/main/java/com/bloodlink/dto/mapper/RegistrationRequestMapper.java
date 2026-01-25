package com.bloodlink.dto.mapper;

import com.bloodlink.dto.request.RegistrationCreateRequest;
import com.bloodlink.dto.response.RegistrationRequestResponse;
import com.bloodlink.entities.RegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for RegistrationRequest entity and DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RegistrationRequestMapper {

    /**
     * Converts create request DTO to RegistrationRequest entity.
     * Note: organization must be set manually after mapping.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "organization", ignore = true)
    RegistrationRequest toEntity(RegistrationCreateRequest request);

    /**
     * Converts RegistrationRequest entity to response DTO.
     */
    @Mapping(source = "organization.name", target = "organizationName")
    RegistrationRequestResponse toResponse(RegistrationRequest entity);
}
