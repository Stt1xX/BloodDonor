package com.bloodlink.repositories;

import com.bloodlink.entities.User;
import com.bloodlink.entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
}
