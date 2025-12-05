package com.example.sucursal_api.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Log logger = LogFactory.getLog(JwtFilter.class);

    private final SecretKey key;

    public JwtFilter(@Value("${security.jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String p = request.getRequestURI();
        return ("/api/auth/login".equals(p) || "/api/auth/refresh".equals(p) || p.startsWith("/files/") || p.startsWith("/chatbot/api/"));
    }



    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        logger.info("JwtFilter: URI=" + request.getRequestURI() + ", Auth header present=" + (authHeader != null));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("JwtFilter: No Bearer token found");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("No autorizado: token ausente");
            return;
        }

        String jwt = authHeader.substring(7);
        logger.info("JwtFilter: Token received (first 20 chars): " + jwt.substring(0, Math.min(20, jwt.length())));

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            logger.info("JwtFilter: Token valid! Subject: " + claims.getSubject());

            String username = claims.getSubject(); // "sub"
            String uidStr = String.valueOf(claims.get("uid"));
            String eidStr = String.valueOf(claims.get("eid"));

            List<String> roles = Optional.ofNullable((List<?>) claims.get("roles"))
                    .orElse(Collections.emptyList())
                    .stream().map(Object::toString).toList();

            var authorities = roles.stream()
                    .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            Map<String, Object> details = new HashMap<>();
            details.put("uid", uidStr);
            details.put("eid", eidStr);
            details.put("username", username);
            authToken.setDetails(details);

            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (Exception e) {
            logger.error("Token inválido: " + e.getMessage());
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("No autorizado: token inválido");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
