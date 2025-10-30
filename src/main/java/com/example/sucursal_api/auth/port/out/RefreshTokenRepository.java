package com.example.sucursal_api.auth.port.out;



import com.example.sucursal_api.auth.domain.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken token);

    Optional<RefreshToken> findValidByToken(String token);

    void revokeById(UUID id);
}
