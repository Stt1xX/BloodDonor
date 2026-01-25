package com.bloodlink.dto.mapper;

import com.bloodlink.dto.request.BloodRequestCreateRequest;
import com.bloodlink.dto.response.BloodRequestBankResponse;
import com.bloodlink.dto.response.BloodRequestMedicalResponse;
import com.bloodlink.entities.BloodRequest;
import com.bloodlink.entities.RequestToBank;
import com.bloodlink.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct mapper for BloodRequest entity and DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, 
        uses = {OrganizationMapper.class, UserMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BloodRequestMapper {

    /**
     * Converts create request DTO to BloodRequest entity.
     * Note: medicalInstitution and creator must be set manually after mapping.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medicalInstitution", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "bankRequests", ignore = true)
    @Mapping(source = "rhesusFactor", target = "rhFactor")
    BloodRequest toEntity(BloodRequestCreateRequest request);
    
    /**
     * Creates BloodRequest entity from DTO with caller as creator.
     */
    default BloodRequest toEntity(BloodRequestCreateRequest request, User caller) {
        BloodRequest entity = toEntity(request);
        entity.setMedicalInstitution(caller.getOrganization());
        entity.setCreator(caller);
        return entity;
    }

    /**
     * Converts RequestToBank to bank response DTO (for blood bank view).
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "request.id", target = "requestId")
    @Mapping(source = "request.bloodGroup", target = "bloodGroup")
    @Mapping(source = "request.rhFactor", target = "rhesusFactor")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "request.volumeNeeded", target = "volumeNeeded")
    @Mapping(source = "request.isEmergency", target = "isEmergency")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "rejectionReason", target = "rejectionReason")
    @Mapping(source = "request.medicalInstitution", target = "institution")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "banker", target = "banker")
    @Mapping(source = "request.creator", target = "creator")
    BloodRequestBankResponse toBankResponse(RequestToBank requestToBank);

    /**
     * Converts RequestToBank to medical response DTO (for medical institution view).
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "request.id", target = "requestId")
    @Mapping(source = "request.bloodGroup", target = "bloodGroup")
    @Mapping(source = "request.rhFactor", target = "rhesusFactor")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "request.volumeNeeded", target = "volumeNeeded")
    @Mapping(source = "request.isEmergency", target = "isEmergency")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "rejectionReason", target = "rejectionReason")
    @Mapping(source = "bloodBank", target = "bank")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "banker", target = "banker")
    @Mapping(source = "request.creator", target = "creator")
    BloodRequestMedicalResponse toMedicalResponse(RequestToBank requestToBank);
}
