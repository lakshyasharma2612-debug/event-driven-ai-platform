package com.analysis.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
@Service
@Slf4j
public class DocumentVectorStoreService {

    private final VectorStore vectorStore;
    private final int topK;
    private final double similarityThreshold;

    public DocumentVectorStoreService(
            VectorStore vectorStore,
            @Value("${app.vector-search.top-k}") int topK,
            @Value("${app.vector-search.similarity-threshold}") double similarityThreshold) {

        this.vectorStore = vectorStore;
        this.topK = topK;
        this.similarityThreshold = similarityThreshold;
    }

    public void store(List<Document> documents) {
        log.debug("Storing {} documents to vector store", documents.size());
        vectorStore.add(documents);
    }

    public List<Document> search(String prompt, String fileId) {
        log.debug("Searching vector store fileId={} promptLength={}", fileId, prompt != null ? prompt.length() : 0);
        SearchRequest request = SearchRequest.builder()
                .query(prompt)
                .topK(topK)
                .similarityThreshold(similarityThreshold)
                .filterExpression("fileId == '" + fileId + "'")
                .build();

        return vectorStore.similaritySearch(request);
    }

    public boolean isDocumentReady(String fileId) {

        SearchRequest request = SearchRequest.builder()
                .query("document")
                .topK(2)
                .similarityThreshold(0.4)
                .filterExpression("fileId == '" + fileId + "'")
                .build();

        List<Document> documents =
                vectorStore.similaritySearch(request);

        return !documents.isEmpty();
    }
}