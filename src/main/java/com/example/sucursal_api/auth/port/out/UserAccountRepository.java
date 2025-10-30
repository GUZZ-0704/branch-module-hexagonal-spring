package com.example.sucursal_api.auth.port.out;



import com.example.sucursal_api.auth.domain.UserAccount;

import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository {
    UserAccount save(UserAccount account);

    Optional<UserAccount> findByUsername(String username);

    UserAccount findById(UUID id);

    boolean existsByUsername(String username);
}

