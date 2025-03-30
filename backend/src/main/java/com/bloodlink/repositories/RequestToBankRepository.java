package com.bloodlink.repositories;

import com.bloodlink.entities.RequestToBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RequestToBankRepository extends JpaRepository<RequestToBank, Long>, JpaSpecificationExecutor<RequestToBank> {
}
