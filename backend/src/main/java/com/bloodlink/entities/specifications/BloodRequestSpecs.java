package com.bloodlink.entities.specifications;

import com.bloodlink.entities.BloodRequest;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RequestStatus;
import com.bloodlink.entities.enums.RhFactor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class BloodRequestSpecs {

    public static Specification<BloodRequest> hasBloodType(final BloodGroup bloodGroup) {
        return (root, query, builder) -> builder.equal(root.get("bloodGroup"), bloodGroup);
    }

    public static Specification<BloodRequest> hasRhFactor(final RhFactor rhFactor) {
        return (root, query, builder) -> builder.equal(root.get("rhFactor"), rhFactor);
    }

    public static Specification<BloodRequest> hasOrganization(final Organization organization) {
        return (root, query, builder) -> builder.equal(root.get("organization"), organization);
    }
    public static Specification<BloodRequest> isEmergency(final Boolean isEmergency) {
        return (root, query, builder) -> builder.equal(root.get("isEmergency"), isEmergency);
    }

    public static Specification<BloodRequest> hasAnyStatus(final List<RequestStatus> statuses) {
        return (root, query, builder) -> root.get("status").in(statuses);


    }
}
