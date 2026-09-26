package com.api.service.service;

import com.api.service.event.FileUploadProducer;
import com.event.platform.events.FileUploaded;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    private final FileUploadProducer fileUploadedProducer;
    private final Path uploadDirectory;

    public FileService(
            FileUploadProducer fileUploadedProducer,
            @Value("${app.upload.dir}") String uploadDir) {

        this.fileUploadedProducer = fileUploadedProducer;
        this.uploadDirectory = Paths.get(uploadDir);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        log.info(
                "Uploading file originalFilename={} contentType={} size={}",
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize()
        );

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

        FileUploaded event = new FileUploaded(
                fileId,
                file.getContentType(),
                Instant.now()
        );

        fileUploadedProducer.sendFileUploaded(event);

        log.info(
                "File uploaded fileId={} stored as {}{}",
                fileId,
                fileId,
                extension
        );

        return fileId;
    }

    public boolean fileExists(String fileId) {

    if (fileId == null || fileId.isBlank()) {
        return false;
    }

    // Prevent path traversal
    if (!fileId.matches(
            "^[a-fA-F0-9\\-]{36}$")) {
        return false;
    }

    String[] extensions = {".pdf", ".txt", ".docx"};

    for (String extension : extensions) {

        Path filePath = uploadDirectory.resolve(fileId + extension);

        if (Files.isRegularFile(filePath)) {
            return true;
        }
    }

    return false;
}
}