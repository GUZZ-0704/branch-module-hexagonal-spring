package com.example.sucursal_api.auth.port.out;

public interface PasswordEncoderPort {
    String encode(CharSequence rawPassword);
    boolean matches(CharSequence rawPassword, String encodedPassword);
}

