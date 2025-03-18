package com.bloodlink.repositories;

import com.bloodlink.entities.RegistrationRequest;
import com.bloodlink.entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long>, JpaSpecificationExecutor<RegistrationRequest> {
    Optional<RegistrationRequest> findByEmail(String email);
    Long countByRole(Role role);
}
