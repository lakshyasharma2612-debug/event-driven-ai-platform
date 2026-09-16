package com.analysis.service.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DocumentChunkingService {

    private final TokenTextSplitter splitter;

    public DocumentChunkingService() {
        this.splitter = TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(350)
                .withMinChunkLengthToEmbed(10)
                .withMaxNumChunks(5000)
                .withKeepSeparator(true)
                .build();
    }

    public List<Document> chunk(String text, String fileId) {

        Document document = new Document(
                text,
                Map.of("fileId", fileId)
        );

        List<Document> chunks = splitter.apply(List.of(document));

        for (int i = 0; i < chunks.size(); i++) {
            chunks.get(i).getMetadata().put("chunkIndex", i);
        }

        return chunks;
    }
}