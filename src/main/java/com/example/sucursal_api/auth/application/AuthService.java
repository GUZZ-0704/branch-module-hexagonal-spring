package com.example.sucursal_api.auth.application;

import com.example.sucursal_api.auth.domain.RefreshToken;
import com.example.sucursal_api.auth.domain.UserAccount;
import com.example.sucursal_api.auth.dto.AuthResponseDTO;
import com.example.sucursal_api.auth.dto.LoginRequestDTO;
import com.example.sucursal_api.auth.dto.RefreshRequestDTO;
import com.example.sucursal_api.auth.dto.UserMeResponseDTO;
import com.example.sucursal_api.auth.port.in.AuthUseCase;
import com.example.sucursal_api.auth.port.out.*;
import com.example.sucursal_api.employee.domain.Employee;
import com.example.sucursal_api.employee.port.out.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService implements AuthUseCase {

    private final UserAccountRepository accountRepo;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtEncoderPort jwtEncoder;
    private final CurrentUserPort currentUser;
    private final EmployeeRepository employeeRepo;
    private final RefreshTokenRepository refreshRepo; // opcional, puedes inyectar un stub

    // Configuración simple (muévelo a properties si quieres)
    private static final long ACCESS_TTL_SECONDS = 60L * 60L;       // 1h
    private static final long REFRESH_TTL_SECONDS = 60L * 60L * 24L * 7L; // 7 días

    public AuthService(UserAccountRepository accountRepo,
                       PasswordEncoderPort passwordEncoder,
                       JwtEncoderPort jwtEncoder,
                       CurrentUserPort currentUser,
                       EmployeeRepository employeeRepo,
                       RefreshTokenRepository refreshRepo) {
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.currentUser = currentUser;
        this.employeeRepo = employeeRepo;
        this.refreshRepo = refreshRepo;
    }


    private Map<String, Object> buildAccessClaims(UserAccount acc) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", acc.getUsername());
        claims.put("uid", acc.getId().toString());
        claims.put("eid", acc.getEmployeeId().toString());
        claims.put("roles", acc.getRoles().stream().map(Enum::name).toList());
        claims.put("type", "access");
        claims.put("iat", Instant.now().getEpochSecond());
        return claims;
    }

    private String generateRefreshTokenValue() {
        return UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID();
    }

    private AuthResponseDTO tokensFor(UserAccount acc, boolean includeRefresh) {
        String access = jwtEncoder.encode(buildAccessClaims(acc), ACCESS_TTL_SECONDS);

        String refresh = null;
        if (includeRefresh && refreshRepo != null) {
            Instant exp = Instant.now().plusSeconds(REFRESH_TTL_SECONDS);
            RefreshToken entity = new RefreshToken(
                    null, acc.getId(), generateRefreshTokenValue(), exp, false
            );
            refreshRepo.save(entity);
            refresh = entity.getToken();
        }

        Set<String> roles = acc.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        return new AuthResponseDTO(access, "Bearer", ACCESS_TTL_SECONDS, refresh, roles);
    }

    private void assertEnabled(UserAccount acc) {
        if (!acc.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cuenta deshabilitada");
        }
    }


    @Override
    @Transactional
    public AuthResponseDTO login(LoginRequestDTO dto) {
        UserAccount acc = accountRepo.findByUsername(dto.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        assertEnabled(acc);

        if (!passwordEncoder.matches(dto.password(), acc.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        // actualizamos lastLoginAt
        acc.setLastLoginAt(LocalDateTime.now(ZoneOffset.UTC));
        accountRepo.save(acc);

        return tokensFor(acc, true);
    }

    @Override
    @Transactional(readOnly = true)
    public UserMeResponseDTO me() {
        UUID userId = currentUser.getUserId();

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado" + userId);
        }

        UserAccount acc = accountRepo.findById(userId);
        String fullName = "";
        try {
            Employee emp = employeeRepo.findById(acc.getEmployeeId());
            fullName = (emp.getFirstName() == null ? "" : emp.getFirstName()) +
                    " " +
                    (emp.getLastName() == null ? "" : emp.getLastName());
            fullName = fullName.trim();
        } catch (Exception ignored) { }

        Set<String> roles = acc.getRoles().stream().map(Enum::name).collect(Collectors.toSet());

        return new UserMeResponseDTO(
                acc.getId(),
                acc.getEmployeeId(),
                acc.getUsername(),
                fullName,
                roles
        );
    }

    @Override
    @Transactional
    public AuthResponseDTO refresh(RefreshRequestDTO dto) {
        if (refreshRepo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Refresh token no habilitado");
        }

        var opt = refreshRepo.findValidByToken(dto.refreshToken());
        var token = opt.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido"));

        if (token.isRevoked() || token.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expirado o revocado");
        }

        UserAccount acc = accountRepo.findById(token.getUserAccountId());

        refreshRepo.revokeById(token.getId());

        return tokensFor(acc, true);
    }

    @Override
    @Transactional
    public void changePassword(String currentPassword, String newPassword) {
        UUID userId = currentUser.getUserId();
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }
        UserAccount acc = accountRepo.findById(userId);

        if (!passwordEncoder.matches(currentPassword, acc.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña actual incorrecta");
        }
        if (newPassword == null || newPassword.length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nueva contraseña debe tener al menos 8 caracteres");
        }

        acc.setPasswordHash(passwordEncoder.encode(newPassword));
        accountRepo.save(acc);
    }
}
