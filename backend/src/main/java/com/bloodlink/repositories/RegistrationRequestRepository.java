package com.bloodlink.repositories;

import com.bloodlink.entities.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long> {
}
