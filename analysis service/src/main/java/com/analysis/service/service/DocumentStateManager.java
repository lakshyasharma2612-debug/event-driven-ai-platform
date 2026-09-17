package com.analysis.service.service;

import com.event.platform.events.AnalysisRequested;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentStateManager {

    private final DocumentVectorStoreService documentVectorStoreService;

    private final Map<String, List<AnalysisRequested>> waitingTasks = new HashMap<>();
    private final Map<String, Boolean> readyDocuments = new HashMap<>();
    private final Map<String, Boolean> failedDocuments = new HashMap<>();

    public synchronized boolean addIfNotReady(AnalysisRequested task) {
        String fileId = task.getFileId();

        // Document already failed
        if (failedDocuments.getOrDefault(fileId, false)) {
            return false;
        }

        // Document already marked ready
        if (readyDocuments.getOrDefault(fileId, false)) {
            return false;
        }

        // Recover readiness from Qdrant after restart
        if (documentVectorStoreService.isDocumentReady(fileId)) {
            readyDocuments.put(fileId, true);
            return false;
        }

        waitingTasks
                .computeIfAbsent(fileId, key -> new ArrayList<>())
                .add(task);
        log.info("Task queued waiting for document fileId={} taskId={}", fileId, task.getTaskId());

        return true;
    }

    public synchronized List<AnalysisRequested> documentReady(String fileId) {
        log.info("Document ready fileId={}", fileId);
        readyDocuments.put(fileId, true);
        failedDocuments.remove(fileId);

        List<AnalysisRequested> tasks = waitingTasks.remove(fileId);

        if (tasks == null) {
            return List.of();
        }

        return tasks;
    }

    public synchronized List<AnalysisRequested> documentFailed(String fileId) {
        log.warn("Document failed fileId={}", fileId);
        failedDocuments.put(fileId, true);
        readyDocuments.remove(fileId);

        List<AnalysisRequested> tasks = waitingTasks.remove(fileId);

        if (tasks == null) {
            return List.of();
        }

        return tasks;
    }

    public synchronized boolean isDocumentFailed(String fileId) {
        return failedDocuments.getOrDefault(fileId, false);
    }
}