package com.bloodlink.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.bloodlink.repositories.UserRepository;
import com.bloodlink.entities.User;
import org.springframework.http.ResponseEntity;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getUserInfo() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<?> addUser(User user) {
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
