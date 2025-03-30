package com.bloodlink.entities.specifications;

import com.bloodlink.entities.BloodRequest;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.RequestToBank;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RequestStatus;
import com.bloodlink.entities.enums.RhFactor;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class RequestToBankSpecs {

    public static Specification<RequestToBank> withMedicalInstitution(Organization medicalInstitution) {
        return (root, query, cb) -> {
            if (medicalInstitution == null) {
                return cb.conjunction();
            }
            Join<RequestToBank, BloodRequest> requestJoin = root.join("request");
            return cb.equal(requestJoin.get("medicalInstitution"), medicalInstitution);
        };
    }

    public static Specification<RequestToBank> withBloodGroup(BloodGroup bloodGroup) {
        return (root, query, cb) -> {
            if (bloodGroup == null) {
                return cb.conjunction();
            }
            Join<RequestToBank, BloodRequest> requestJoin = root.join("request");
            return cb.equal(requestJoin.get("bloodGroup"), bloodGroup);
        };
    }

    public static Specification<RequestToBank> withRhFactor(RhFactor rhFactor) {
        return (root, query, cb) -> {
            if (rhFactor == null) {
                return cb.conjunction();
            }
            Join<RequestToBank, BloodRequest> requestJoin = root.join("request");
            return cb.equal(requestJoin.get("rhFactor"), rhFactor);
        };
    }

    public static Specification<RequestToBank> withAnyStatus(List<RequestStatus> statuses) {
        return (root, query, cb) -> {
            if (statuses == null) {
                return cb.conjunction();
            }
            return root.get("status").in(statuses);
        };
    }

    public static Specification<RequestToBank> withFilters(
            Organization medicalInstitution,
            BloodGroup bloodGroup,
            RhFactor rhFactor,
            List<RequestStatus> statuses) {

        return Specification
                .where(withMedicalInstitution(medicalInstitution))
                .and(withBloodGroup(bloodGroup))
                .and(withRhFactor(rhFactor))
                .and(withAnyStatus(statuses));
    }
}