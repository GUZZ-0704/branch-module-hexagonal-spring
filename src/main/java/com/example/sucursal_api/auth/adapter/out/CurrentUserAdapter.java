package com.example.sucursal_api.auth.adapter.out;

import com.example.sucursal_api.auth.port.out.CurrentUserPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CurrentUserAdapter implements CurrentUserPort {

    @Override
    public UUID getUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwt) {
            Object uid = jwt.getToken().getClaims().get("uid");
            return uid != null ? UUID.fromString(uid.toString()) : null;
        }
        return null;
    }

    @Override
    public UUID getEmployeeId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwt) {
            Object eid = jwt.getToken().getClaims().get("eid");
            return eid != null ? UUID.fromString(eid.toString()) : null;
        }
        return null;
    }

    @Override
    public String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }

    @Override
    public Set<String> getRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return Set.of();
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
