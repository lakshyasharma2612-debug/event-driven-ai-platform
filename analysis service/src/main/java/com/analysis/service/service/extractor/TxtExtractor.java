package com.analysis.service.service.extractor;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class TxtExtractor implements DocumentExtractor {

    @Override
    public boolean supports(String contentType) {
        return "text/plain".equalsIgnoreCase(contentType);
    }

    @Override
    public String extract(Path filePath) throws Exception {
        return Files.readString(filePath);
    }
}