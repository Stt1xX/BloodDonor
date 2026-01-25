package com.bloodlink.service;

import com.bloodlink.entities.DTOs.UserDTOto;
import com.bloodlink.entities.User;
import com.bloodlink.entities.specifications.UserSpecs;
import com.bloodlink.exceptions.AccessDeniedException;
import com.bloodlink.exceptions.AuthenticationRequiredException;
import com.bloodlink.exceptions.ResourceNotFoundException;
import com.bloodlink.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User getByEmail(String email) {
        return userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден: " + email));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailAndIsDeletedFalse(email);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByEmailAndIsDeletedFalse(userDetails.getUsername())
                    .orElseThrow(() -> new AuthenticationRequiredException("Не могу распознать текущего пользователя"));
        }
        throw new AuthenticationRequiredException("Пользователь не аутентифицирован");
    }

    public Optional<User> getCurrentUserOptional() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByEmailAndIsDeletedFalse(userDetails.getUsername());
        }
        return Optional.empty();
    }

    public Page<UserDTOto> getAll(String pattern, Pageable page) {
        Specification<User> filters = Specification.where(!StringUtils.hasLength(pattern) ? null :
                        UserSpecs.nameLike(pattern))
                .or(UserSpecs.surnameLike(pattern))
                .or(UserSpecs.emailLike(pattern))
                .and(UserSpecs.notDeleted());
        var p = userRepository.findAll(filters, page);

        return p.map(UserDTOto::convert);
    }

    @Transactional
    public void delete(Long id) {
        User currentUser = getCurrentUser();
        if (currentUser.getId().equals(id)) {
            throw new AccessDeniedException("Вы не можете удалить сами себя!");
        }
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден: " + id));
        
        log.info("Deleting user id={}, email={}", id, user.getEmail());
        user.setIsDeleted(true);
        userRepository.save(user);
    }
}
