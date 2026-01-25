package com.bloodlink.dto.mapper;

import com.bloodlink.dto.response.NotificationResponse;
import com.bloodlink.entities.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for Notification entity and DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {

    /**
     * Converts Notification entity to response DTO.
     */
    @Mapping(source = "read", target = "isRead")
    NotificationResponse toResponse(Notification entity);
}
