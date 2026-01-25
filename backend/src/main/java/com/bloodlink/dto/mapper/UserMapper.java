package com.bloodlink.dto.mapper;

import com.bloodlink.dto.response.UserResponse;
import com.bloodlink.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for User entity and DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    /**
     * Converts User entity to response DTO.
     */
    @Mapping(target = "organizationName", expression = "java(user.getOrganization() != null ? user.getOrganization().getName() : \"\")")
    UserResponse toResponse(User user);
}
