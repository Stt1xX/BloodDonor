package com.bloodlink.entities.specifications;

import com.bloodlink.entities.Notification;
import com.bloodlink.entities.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class NotificationSpecs {


    public static Specification<Notification> withUser(User user) {
        return (root, query, cb) -> {
            if (user == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("user"), user);
        };
    }

    public static Specification<Notification> isRead(Boolean isRead) {
        return (root, query, cb) -> {
            if (isRead == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("isRead"), isRead);
        };
    }

    public static Specification<Notification> withIdIn(List<Long> ids) {
        return (root, query, cb) -> {
            if (ids == null) {
                return cb.conjunction();
            }
            return root.get("id").in(ids);
        };
    }
}