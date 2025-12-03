package com.example.sucursal_api.auth.adapter.out;

import com.example.sucursal_api.auth.port.out.CurrentUserPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CurrentUserAdapter implements CurrentUserPort {

    @Override
    public UUID getUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        
        // Obtener de details (set by JwtFilter)
        Object details = auth.getDetails();
        if (details instanceof Map<?, ?> map) {
            Object uid = map.get("uid");
            if (uid != null) {
                return UUID.fromString(uid.toString());
            }
        }
        return null;
    }

    @Override
    public UUID getEmployeeId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        
        // Obtener de details (set by JwtFilter)
        Object details = auth.getDetails();
        if (details instanceof Map<?, ?> map) {
            Object eid = map.get("eid");
            if (eid != null) {
                return UUID.fromString(eid.toString());
            }
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
