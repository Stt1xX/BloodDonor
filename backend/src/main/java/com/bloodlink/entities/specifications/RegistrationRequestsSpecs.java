package com.bloodlink.entities.specifications;

import com.bloodlink.entities.RegistrationRequest;
import jakarta.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

public class RegistrationRequestsSpecs {

    public static Specification<RegistrationRequest> nameLike(final String name) {
        return (root, query, builder) ->builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<RegistrationRequest> surnameLike(final String surname) {
        return (root, query, builder) ->builder.like(root.get("surname"), "%" + surname + "%");
    }

    public static Specification<RegistrationRequest> emailLike(final String email) {
        return (root, query, builder) ->builder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<RegistrationRequest> roleLike(final String role) {
        return (root, query, builder) ->builder.like(root.get("role"), "%" + role + "%");
    }

}
