package com.example.sucursal_api.auth.port.out;


import java.util.Map;

public interface JwtEncoderPort {
    /** Genera un JWT con los claims indicados y expiración en segundos. */
    String encode(Map<String, Object> claims, long expiresInSeconds);

    /** (Opcional) Valida y retorna claims si necesitas decodificar en algún punto. */
    default Map<String, Object> decode(String token) { return Map.of(); }
}
