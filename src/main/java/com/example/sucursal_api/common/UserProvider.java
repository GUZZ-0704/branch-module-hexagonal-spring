package com.example.sucursal_api.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserProvider {

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;

        String username = auth.getName();

        Set<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(a -> a.startsWith("ROLE_") ? a.substring(5) : a)
                .collect(Collectors.toSet());

        UUID uid = null;
        UUID eid = null;
        Object details = auth.getDetails();
        if (details instanceof java.util.Map<?, ?> map) {
            Object uidObj = map.get("uid");
            Object eidObj = map.get("eid");
            if (uidObj != null) uid = UUID.fromString(uidObj.toString());
            if (eidObj != null) eid = UUID.fromString(eidObj.toString());
        }

        return new User(uid, eid, username, roles);
    }
}
