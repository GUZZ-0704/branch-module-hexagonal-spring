package com.example.sucursal_api.auth.adapter.in;


import com.example.sucursal_api.auth.dto.AuthResponseDTO;
import com.example.sucursal_api.auth.dto.LoginRequestDTO;
import com.example.sucursal_api.auth.dto.RefreshRequestDTO;
import com.example.sucursal_api.auth.dto.UserMeResponseDTO;
import com.example.sucursal_api.auth.port.in.AuthUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthUseCase useCase;

    public AuthController(AuthUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(useCase.login(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeResponseDTO> me() {
        return ResponseEntity.ok(useCase.me());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@Valid @RequestBody RefreshRequestDTO dto) {
        return ResponseEntity.ok(useCase.refresh(dto));
    }

    public record ChangePasswordDTO(String currentPassword, String newPassword) {}
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO body) {
        useCase.changePassword(body.currentPassword(), body.newPassword());
        return ResponseEntity.noContent().build();
    }
}

