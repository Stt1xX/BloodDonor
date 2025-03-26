package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
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

    public static OrganizationDTOto convert(Organization organization) {
        return new OrganizationDTOto(organization.getId(), organization.getName(), organization.getAddress(),
                organization.getPhone(), organization.getHoursFrom(), organization.getHoursTo(),
                organization.getMinutesFrom(), organization.getMinutesTo(), organization.getType());
    }
}
