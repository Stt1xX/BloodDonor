package com.bloodlink.service;

import com.bloodlink.entities.User;
import com.bloodlink.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

//    public User save(){
//
//    }
}
