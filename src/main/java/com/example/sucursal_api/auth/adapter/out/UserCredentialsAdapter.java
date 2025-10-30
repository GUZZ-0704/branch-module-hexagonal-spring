package com.example.sucursal_api.auth.adapter.out;


import com.example.sucursal_api.auth.port.out.PasswordEncoderPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserCredentialsAdapter implements PasswordEncoderPort {

    private final PasswordEncoder pe;

    public UserCredentialsAdapter(PasswordEncoder pe) {
        this.pe = pe;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return pe.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return pe.matches(rawPassword, encodedPassword);
    }
}
