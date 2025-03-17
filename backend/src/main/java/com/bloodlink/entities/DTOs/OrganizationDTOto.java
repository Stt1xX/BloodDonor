package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.Organization;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class OrganizationDTOto {
    private Long id;
    private String name;
    private String address;

    public OrganizationDTOto(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public static <E extends Organization> OrganizationDTOto convert(E entity){
        return new OrganizationDTOto(entity.getId(), entity.getName(), entity.getAddress());
    }

    public static <E extends Organization> List<OrganizationDTOto> convertList(List<E> entities) {
        return entities.stream()
                .map(OrganizationDTOto::convert)
                .collect(Collectors.toList());
    }
}
