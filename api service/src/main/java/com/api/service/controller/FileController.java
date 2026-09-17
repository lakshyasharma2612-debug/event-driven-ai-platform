package com.api.service.controller;

import com.api.service.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file) throws IOException {
        log.debug("Received file upload filename={}", file.getOriginalFilename());
        String fileId = fileService.uploadFile(file);

        return ResponseEntity.ok(fileId);
    }
}