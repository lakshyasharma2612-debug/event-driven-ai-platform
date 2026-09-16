package com.analysis.service.service;

import com.analysis.service.service.extractor.DocumentExtractor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class DocumentIngestionService {

    private final List<DocumentExtractor> extractors;

    public DocumentIngestionService(List<DocumentExtractor> extractors) {
        this.extractors = extractors;
    }

    public String extractText(Path filePath, String contentType) throws Exception {

        DocumentExtractor extractor = extractors.stream()
                .filter(e -> e.supports(contentType))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unsupported document type: " + contentType
                        )
                );

        return extractor.extract(filePath);
    }
}