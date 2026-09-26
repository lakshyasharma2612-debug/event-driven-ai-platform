package com.analysis.service.service.extractor;

import java.io.IOException;
import java.nio.file.Path;

public interface DocumentExtractor {

    boolean supports(String contentType);

    String extract(Path filePath) throws IOException;
}