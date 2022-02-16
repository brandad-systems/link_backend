package io.example.utils;

import io.example.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {

    public static User getUser() {

        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.getPrincipal() != null
                && authentication.getPrincipal() instanceof User) {
            user = (User) authentication.getPrincipal();

        }
        return user;

    }
}
