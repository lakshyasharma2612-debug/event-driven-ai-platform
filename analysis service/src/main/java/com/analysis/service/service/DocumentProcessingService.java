package com.analysis.service.service;


import com.event.platform.events.AnalysisRequested;
import com.event.platform.events.FileUploaded;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentProcessingService {

    private final DocumentIngestionService documentIngestionService;
    private final DocumentChunkingService documentChunkingService;
    private final DocumentVectorStoreService documentVectorStoreService;
    private final DocumentStateManager documentStateManager;
    private final AnalysisTaskProcessor taskProcessor;

    public void process(FileUploaded event) throws Exception {
        log.info("Processing document fileId={} contentType={}", event.getFileId(), event.getContentType());
        if (documentVectorStoreService.isDocumentReady(event.getFileId())) {
                log.info("Document already ready fileId={}", event.getFileId());
                documentStateManager.documentReady(event.getFileId());
                return;
                }

        Path filePath = Paths.get(
                "..",
                "uploads",
                event.getFileId() + getExtension(event.getContentType())
        );

        String extractedText =
                documentIngestionService.extractText(
                        filePath,
                        event.getContentType()
                );
        log.info("Extraction completed fileId={} chars={}", event.getFileId(), extractedText.length());

        List<Document> chunks =
                documentChunkingService.chunk(
                        extractedText,
                        event.getFileId()
                );
        log.info("Created {} chunks for fileId={}", chunks.size(), event.getFileId());

        documentVectorStoreService.store(chunks);
        log.info("Document stored in vector store fileId={}", event.getFileId());

        List<AnalysisRequested> waitingTasks =
                documentStateManager.documentReady(event.getFileId());
        log.info("Resuming {} waiting tasks for fileId={}", waitingTasks.size(), event.getFileId());

        for (AnalysisRequested task : waitingTasks) {
            taskProcessor.process(task);
        }
    }

    private String getExtension(String contentType) {

        return switch (contentType) {
            case "application/pdf" -> ".pdf";
            case "text/plain" -> ".txt";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                    -> ".docx";
            default -> throw new IllegalArgumentException(
                    "Unsupported document type"
            );
        };
    }
}