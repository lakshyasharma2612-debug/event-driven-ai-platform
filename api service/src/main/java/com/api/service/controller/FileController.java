package com.api.service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {


    private final Path uploadDirectory = Paths.get("uploads");

    @PostMapping
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file) throws IOException {

        Files.createDirectories(uploadDirectory);

        String fileId = UUID.randomUUID().toString();

        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(
                    originalFilename.lastIndexOf(".")
            );
        }

        Path filePath = uploadDirectory.resolve(fileId + extension);

        file.transferTo(filePath);
        

        return ResponseEntity.ok(fileId);
    }
}