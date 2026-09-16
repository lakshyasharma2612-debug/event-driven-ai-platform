package com.analysis.service.service.extractor;

import java.nio.file.Path;

public interface DocumentExtractor {

    boolean supports(String contentType);

    String extract(Path filePath) throws Exception;
}