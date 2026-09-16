package com.api.service.service;

import com.api.service.event.FileUploadProducer;
import com.event.platform.events.FileUploaded;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

@Service
public class FileService {

    private final FileUploadProducer fileUploadedProducer;

    private final Path uploadDirectory = Paths.get("uploads");

    public FileService(FileUploadProducer fileUploadedProducer) {
        this.fileUploadedProducer = fileUploadedProducer;
    }

    public String uploadFile(MultipartFile file) throws IOException {

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

        fileUploadedProducer.sebdFileupload(event);

        return fileId;
    }
}