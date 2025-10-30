package com.example.sucursal_api.auth.adapter.out;

import com.example.sucursal_api.auth.port.out.JwtEncoderPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtEncoderImpl implements JwtEncoderPort {

    private final SecretKey key;

    public JwtEncoderImpl(@Value("${security.jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String encode(Map<String, Object> claims, long expiresInSeconds) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expiresInSeconds)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
