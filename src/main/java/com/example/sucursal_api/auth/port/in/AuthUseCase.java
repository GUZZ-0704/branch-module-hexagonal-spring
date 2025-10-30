package com.example.sucursal_api.auth.port.in;


import com.example.sucursal_api.auth.dto.AuthResponseDTO;
import com.example.sucursal_api.auth.dto.LoginRequestDTO;
import com.example.sucursal_api.auth.dto.RefreshRequestDTO;
import com.example.sucursal_api.auth.dto.UserMeResponseDTO;

public interface AuthUseCase {
    AuthResponseDTO login(LoginRequestDTO dto);

    UserMeResponseDTO me();

    AuthResponseDTO refresh(RefreshRequestDTO dto);

    void changePassword(String currentPassword, String newPassword);
}
