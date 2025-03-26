package com.bloodlink.service;

import com.bloodlink.entities.DTOs.OrganizationDTOto;
import com.bloodlink.entities.DTOs.UserDTOto;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.User;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.enums.Role;
import com.bloodlink.entities.specifications.OrganizationSpecs;
import com.bloodlink.entities.specifications.UserSpecs;
import com.bloodlink.exceptions.CustomDuplicateException;
import com.bloodlink.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
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
public class UserService {

    private final UserRepository userRepository;

    public User getByEmail(String email) {
        return userRepository.findByEmailAndIsDeletedFalse(email).orElse(null);
    }

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object var3 = authentication.getPrincipal();
            if (var3 instanceof UserDetails userDetails) {
                return this.userRepository.findByEmailAndIsDeletedFalse(userDetails.getUsername());
            }
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
    public String delete(Long id) throws CustomDuplicateException {
        if (getCurrentUser().isPresent() && getCurrentUser().get().getId().equals(id)) {
            return "Вы не можете удалить сами себя!";
        }
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Пользователь с таким id уже существует");
        }
        user.get().setIsDeleted(true);
        userRepository.save(user.get());
        return "Пользователь успешно удален!";
    }
}
