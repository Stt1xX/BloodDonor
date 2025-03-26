package com.bloodlink.repositories;

import com.bloodlink.entities.User;
import com.bloodlink.entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {
    Long countByRole(Role role);
    Optional<User> findByEmailAndIsDeletedFalse(String email);
}
