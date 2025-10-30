package com.example.sucursal_api.auth.adapter.out;

import com.example.sucursal_api.auth.domain.UserAccount;
import com.example.sucursal_api.auth.port.out.UserAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserAccountRepositoryImpl implements UserAccountRepository {

    private final UserAccountJpaRepository jpa;

    public UserAccountRepositoryImpl(UserAccountJpaRepository jpa) {
        this.jpa = jpa;
    }

    private UserAccount toDomain(UserAccountEntity e) {
        return new UserAccount(
                e.getId(),
                e.getEmployeeId(),
                e.getUsername(),
                e.getPasswordHash(),
                e.isEnabled(),
                e.getRoles(),
                e.getLastLoginAt()
        );
    }

    private void copyToEntity(UserAccount src, UserAccountEntity e) {
        e.setEmployeeId(src.getEmployeeId());
        e.setUsername(src.getUsername());
        e.setPasswordHash(src.getPasswordHash());
        e.setEnabled(src.isEnabled());
        e.setRoles(src.getRoles());
        e.setLastLoginAt(src.getLastLoginAt());
    }

    private UserAccountEntity toEntity(UserAccount d) {
        UserAccountEntity e = new UserAccountEntity();
        if (d.getId() != null) e.setId(d.getId());
        copyToEntity(d, e);
        return e;
    }

    @Override
    public UserAccount save(UserAccount account) {
        UserAccountEntity entity = toEntity(account);
        UserAccountEntity saved = jpa.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        return jpa.findByUsername(username).map(this::toDomain);
    }

    @Override
    public UserAccount findById(UUID id) {
        return jpa.findById(id).map(this::toDomain)
                .orElseThrow(() -> new java.util.NoSuchElementException("UserAccount no encontrado: " + id));
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpa.existsByUsername(username);
    }
}

