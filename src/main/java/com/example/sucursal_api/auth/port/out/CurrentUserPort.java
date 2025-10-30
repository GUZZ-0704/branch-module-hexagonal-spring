package com.example.sucursal_api.auth.port.out;


import java.util.Set;
import java.util.UUID;

public interface CurrentUserPort {
    UUID getUserId();
    UUID getEmployeeId();
    String getUsername();
    Set<String> getRoles();
}
