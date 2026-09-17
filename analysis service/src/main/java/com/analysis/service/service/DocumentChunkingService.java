package com.analysis.service.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
@Service
public class DocumentChunkingService {

    private final TokenTextSplitter splitter;

    public DocumentChunkingService(
            @Value("${app.document.chunk-size}") int chunkSize,
            @Value("${app.document.min-chunk-size-chars}") int minChunkSizeChars,
            @Value("${app.document.min-chunk-length-to-embed}") int minChunkLengthToEmbed,
            @Value("${app.document.max-num-chunks}") int maxNumChunks) {

        this.splitter = TokenTextSplitter.builder()
                .withChunkSize(chunkSize)
                .withMinChunkSizeChars(minChunkSizeChars)
                .withMinChunkLengthToEmbed(minChunkLengthToEmbed)
                .withMaxNumChunks(maxNumChunks)
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