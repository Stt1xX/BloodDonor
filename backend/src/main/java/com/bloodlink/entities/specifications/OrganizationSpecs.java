package com.bloodlink.entities.specifications;

import com.bloodlink.entities.Organization;

import com.bloodlink.entities.enums.OrganizationType;
import org.springframework.data.jpa.domain.Specification;

public class OrganizationSpecs {

    public static Specification<Organization> nameLike(final String name) {
        return (root, query, builder) ->builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Organization> addressLike(final String address) {
        return (root, query, builder) ->builder.like(root.get("address"), "%" + address + "%");
    }

    public static Specification<Organization> phoneLike(final String phone) {
        return (root, query, builder) ->builder.like(root.get("phone"), "%" + phone + "%");
    }

    public static Specification<Organization> hasType(OrganizationType type) {
        return (root, query, criteriaBuilder) ->
                type == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("type"), type);
    }
}
