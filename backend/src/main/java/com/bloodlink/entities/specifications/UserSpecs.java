package com.bloodlink.entities.specifications;

import com.bloodlink.entities.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecs {

    public static Specification<User> nameLike(final String name) {
        return (root, query, builder) ->builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<User> surnameLike(final String address) {
        return (root, query, builder) ->builder.like(root.get("surname"), "%" + address + "%");
    }

    public static Specification<User> emailLike(final String phone) {
        return (root, query, builder) ->builder.like(root.get("email"), "%" + phone + "%");
    }

    public static Specification<User> notDeleted() {
        return (root, query, builder) ->builder.equal(root.get("isDeleted"), false);
    }
}
