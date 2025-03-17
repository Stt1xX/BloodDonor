package com.bloodlink.repositories;

import com.bloodlink.entities.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long>, JpaSpecificationExecutor<RegistrationRequest> {
}
