package com.example.sucursal_api.image.adapter.in;

import com.example.sucursal_api.file.StorageService;
import com.example.sucursal_api.file.StoredFile;
import com.example.sucursal_api.image.domain.BranchImage;
import com.example.sucursal_api.image.dto.BranchImageResponseDTO;
import com.example.sucursal_api.image.port.out.BranchImageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/branches/{branchId}/images")
public class BranchImageUploadController {

    private final StorageService storage;
    private final BranchImageRepository imageRepo;

    public BranchImageUploadController(StorageService storage, BranchImageRepository imageRepo) {
        this.storage = storage;
        this.imageRepo = imageRepo;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<BranchImageResponseDTO> upload(
            @PathVariable UUID branchId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "altText", required = false) String altText,
            @RequestParam(value = "cover", required = false, defaultValue = "false") boolean cover
    ) {

        StoredFile stored = storage.store(file, "branch/" + branchId);

        BranchImage img = new BranchImage(
                null,
                branchId,
                stored.url(),
                title,
                altText,
                cover
        );
        var saved = imageRepo.save(img);

        BranchImageResponseDTO dto = new BranchImageResponseDTO(
                saved.getId(), saved.getBranchId(), saved.getUrl(),
                saved.getTitle(), saved.getAltText(), saved.isCover()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
