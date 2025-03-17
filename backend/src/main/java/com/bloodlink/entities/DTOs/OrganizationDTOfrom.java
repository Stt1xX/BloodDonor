package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.Organization;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class OrganizationDTOfrom {
    private Long id;
    private String name;
    private String address;

    public OrganizationDTOfrom(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public static <E extends Organization> List<OrganizationDTOfrom> convertList(List<E> entities) {
        return entities.stream()
                .map(entity -> new OrganizationDTOfrom(entity.getId(), entity.getName(), entity.getAddress()))
                .collect(Collectors.toList());
    }
}
