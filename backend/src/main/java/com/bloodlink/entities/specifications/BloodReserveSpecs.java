package com.bloodlink.entities.specifications;

import com.bloodlink.entities.BloodReserve;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import org.springframework.data.jpa.domain.Specification;

public class BloodReserveSpecs {

    public static Specification<BloodReserve> hasBloodType(final BloodGroup bloodGroup) {
        return (root, query, builder) ->builder.equal(root.get("id").get("bloodGroup"), bloodGroup);
    }

    public static Specification<BloodReserve> hasRhFactor(final RhFactor rhFactor) {
        return (root, query, builder) ->builder.equal(root.get("id").get("rhFactor"), rhFactor);
    }

    public static Specification<BloodReserve> hasOrganization(final Organization organization) {
        return (root, query, builder) -> builder.equal(root.get("bank"), organization);
    }
}
