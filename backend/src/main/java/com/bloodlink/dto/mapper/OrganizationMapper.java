package com.bloodlink.dto.mapper;

import com.bloodlink.dto.request.OrganizationCreateRequest;
import com.bloodlink.dto.response.OrganizationResponse;
import com.bloodlink.entities.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for Organization entity and DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationMapper {

    /**
     * Converts create request DTO to Organization entity.
     */
    @Mapping(target = "members", ignore = true)
    @Mapping(source = "type", target = "type")
    Organization toEntity(OrganizationCreateRequest request);

    /**
     * Converts Organization entity to response DTO.
     */
    @Mapping(source = "type", target = "organizationType")
    OrganizationResponse toResponse(Organization entity);

    /**
     * Updates existing entity from request DTO.
     */
    @Mapping(target = "members", ignore = true)
    @Mapping(source = "type", target = "type")
    void updateEntityFromRequest(OrganizationCreateRequest request, @MappingTarget Organization entity);
}
