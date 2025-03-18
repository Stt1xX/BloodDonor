package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.BloodBank;
import com.bloodlink.entities.MedicalInstitution;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.OrganizationType;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class OrganizationDTOto {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private Integer hoursFrom;
    private Integer hoursTo;
    private Integer minutesFrom;
    private Integer minutesTo;
    private OrganizationType organizationType;

    public OrganizationDTOto(Long id, String address, String name, String phone, Integer hoursFrom,
                             Integer hoursTo, Integer minutesFrom, Integer minutesTo, OrganizationType organizationType) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.hoursFrom = hoursFrom;
        this.phone = phone;
        this.hoursTo = hoursTo;
        this.minutesFrom = minutesFrom;
        this.minutesTo = minutesTo;
        this.organizationType = organizationType;
    }

    public static OrganizationDTOto convert(Organization organization, OrganizationType organizationType) {
        return new OrganizationDTOto(organization.getId(), organization.getAddress(), organization.getName(),
                organization.getPhone(), organization.getHoursFrom(), organization.getHoursTo(),
                organization.getMinutesFrom(), organization.getMinutesTo(), organizationType);
    }
}
