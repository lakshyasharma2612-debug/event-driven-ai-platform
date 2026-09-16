package com.analysis.service.event;

import com.analysis.service.service.DocumentChunkingService;
import com.analysis.service.service.DocumentIngestionService;
import com.analysis.service.service.DocumentVectorStoreService;
import com.event.platform.events.AnalysisRequested;
import com.event.platform.events.FileUploaded;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.document.Document;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor 
public class FileUploadedConsumer {

    private final DocumentIngestionService documentIngestionService;
    private final DocumentChunkingService documentChunkingService;
    private final DocumentVectorStoreService documentVectorStoreService;
    private final AnalysisRequestConsumer analysisRequestConsumer;
    private final DocumentStateManager documentStateManager;    
    @KafkaListener(
            topics = "file-uploaded",
            groupId = "document-ingestion"
    )
    public void consume(FileUploaded event) {

        System.out.println(
                "File uploaded event received: " + event.getFileId()
        );

        Path filePath = Paths.get(
                "uploads",
                event.getFileId() + getExtension(event.getContentType())
        );

        try {

            // 1. Extract
            String extractedText =
                    documentIngestionService.extractText(
                            filePath,
                            event.getContentType()
                    );

            System.out.println("Extraction completed.");

            // 2. Chunk
            List<Document> chunks =
                    documentChunkingService.chunk(
                            extractedText,
                            event.getFileId()
                    );

            System.out.println(
                    "Created " + chunks.size() + " chunks."
            );

            // 3. Embed + store in Qdrant
            documentVectorStoreService.store(chunks);

            System.out.println(
                    "Document stored in Qdrant: " + event.getFileId()
            );

           List<AnalysisRequested> tasks =
                        documentStateManager.documentReady(event.getFileId());

                for (AnalysisRequested task : tasks) {
                analysisRequestConsumer.processTask(task);
                }

        } catch (Exception e) {

            System.err.println(
                    "Failed to process file: " + event.getFileId()
            );

            e.printStackTrace();
        }
    }

    private String getExtension(String contentType) {

        return switch (contentType) {

            case "application/pdf" -> ".pdf";

            case "text/plain" -> ".txt";

            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                    -> ".docx";

            default -> throw new IllegalArgumentException(
                    "Unsupported content type: " + contentType
            );
        };
    }
}