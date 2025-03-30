package com.bloodlink.entities.specifications;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import org.springframework.data.jpa.domain.Specification;

public class BloodUnitsSpecs {

    public static Specification<BloodUnit> hasBloodType(final BloodGroup bloodGroup) {
        return (root, query, builder) ->builder.equal(root.get("bloodGroup"), bloodGroup);
    }

    public static Specification<BloodUnit> hasRhFactor(final RhFactor rhFactor) {
        return (root, query, builder) ->builder.equal(root.get("rhFactor"), rhFactor);
    }

    public static Specification<BloodUnit> hasOrganization(final Organization organization) {
        return (root, query, builder) -> builder.equal(root.get("bloodBank"), organization);
    }

}
