package com.example.sucursal_api.file;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    StoredFile store(MultipartFile file, String subfolder);
    void delete(String relativePath);
}

