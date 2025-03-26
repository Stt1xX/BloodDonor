package com.bloodlink.repositories;

import com.bloodlink.entities.User;
import com.bloodlink.entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Long countByRole(Role role);
}
