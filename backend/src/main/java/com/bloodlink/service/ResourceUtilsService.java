package com.bloodlink.service;

import com.bloodlink.entities.User;
import com.bloodlink.exceptions.CallerIsNotAUserException;
import com.bloodlink.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ResourceUtilsService {

    private final UserRepository userRepository;

    public ResourceUtilsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCaller() throws CallerIsNotAUserException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmailAndIsDeletedFalse(auth.getName())
                .orElseThrow(() -> new CallerIsNotAUserException("Вызывающий не является зарегистрированным пользователем"));
    }

}
