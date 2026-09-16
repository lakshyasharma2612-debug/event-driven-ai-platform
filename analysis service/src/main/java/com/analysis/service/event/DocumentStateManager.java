package com.analysis.service.event;

import com.event.platform.events.AnalysisRequested;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentStateManager {

    private final Map<String, List<AnalysisRequested>> waiting =
            new HashMap<>();

    private final Map<String, Boolean> ready =
            new HashMap<>();

    public synchronized boolean isReady(String fileId) {
        return ready.getOrDefault(fileId, false);
    }

   public synchronized boolean addIfNotReady(AnalysisRequested task) {

        if (ready.getOrDefault(task.getFileId(), false)) {
            return false;
        }

        waiting
                .computeIfAbsent(task.getFileId(), key -> new ArrayList<>())
                .add(task);

        return true;
    }

    public synchronized List<AnalysisRequested> documentReady(String fileId) {

        ready.put(fileId, true);

        List<AnalysisRequested> tasks = waiting.remove(fileId);

        if (tasks == null) {
            return List.of();
        }

        return tasks;
    }
}