package com.bloodlink.entities.specifications;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.OrganizationType;
import org.springframework.data.jpa.domain.Specification;

public class BloodUnitsSpecs {

    public static Specification<BloodUnit> bloodTypeLike(final String bloodType) {
        return (root, query, builder) ->builder.like(root.get("bloodType"), "%" + bloodType + "%");
    }

    public static Specification<BloodUnit> rhFactorLike(final String rhFactor) {
        return (root, query, builder) ->builder.like(root.get("rhFactor"), "%" + rhFactor + "%");
    }
}
