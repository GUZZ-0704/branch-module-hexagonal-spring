package com.example.sucursal_api.file;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Service
public class FileSystemStorageService implements StorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg","image/png","image/webp","image/gif"
    );

    private final Path root;
    private final String publicPrefix;

    public FileSystemStorageService(@Value("${app.storage.root}") String root,
                                    @Value("${app.storage.public-prefix:/files/}") String publicPrefix) throws IOException {
        this.root = Paths.get(root).toAbsolutePath().normalize();
        this.publicPrefix = publicPrefix;
        Files.createDirectories(this.root);
    }

    @Override
    public StoredFile store(MultipartFile file, String subfolder) {
        if (file.isEmpty()) throw new IllegalArgumentException("Archivo vacío");
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Tipo de archivo no permitido");
        }

        String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "image" : file.getOriginalFilename());
        String ext = "." + FilenameUtils.getExtension(original).toLowerCase();
        if (ext.equals(".")) ext = ".bin";

        LocalDate today = LocalDate.now();
        Path folder = this.root
                .resolve(subfolder)
                .resolve(String.valueOf(today.getYear()))
                .resolve(String.format("%02d", today.getMonthValue()));

        try {
            Files.createDirectories(folder);
            String filename = UUID.randomUUID() + ext;
            Path target = folder.resolve(filename).normalize();

            if (!target.startsWith(this.root)) throw new SecurityException("Ruta inválida");

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            Path relative = this.root.relativize(target);
            String url = publicPrefix + relative.toString().replace("\\", "/");
            return new StoredFile(relative.toString().replace("\\", "/"), filename, url);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando archivo", e);
        }
    }

    @Override
    public void delete(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) return;
        Path p = this.root.resolve(relativePath).normalize();
        if (!p.startsWith(this.root)) throw new SecurityException("Ruta inválida");
        try { Files.deleteIfExists(p); } catch (IOException ignored) {}
    }
}
