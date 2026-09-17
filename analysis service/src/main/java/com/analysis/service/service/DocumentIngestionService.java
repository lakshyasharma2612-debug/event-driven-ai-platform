package com.analysis.service.service;

import com.analysis.service.service.extractor.DocumentExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentIngestionService {

    private final List<DocumentExtractor> extractors;

    public String extractText(Path filePath, String contentType) throws Exception {

        DocumentExtractor extractor = extractors.stream()
                .filter(e -> e.supports(contentType))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unsupported document type: " + contentType
                        )
                );

        log.debug("Extracting text filePath={} contentType={}", filePath, contentType);
        return extractor.extract(filePath);
    }
}