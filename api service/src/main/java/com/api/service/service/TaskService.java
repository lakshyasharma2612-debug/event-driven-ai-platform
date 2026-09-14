package com.api.service.service;

import com.api.service.event.AnalysisRequestProducer;
import com.event.platform.events.AnalysisRequested;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TaskService {

    private final AnalysisRequestProducer analysisRequestProducer;

    public TaskService(AnalysisRequestProducer analysisRequestProducer) {
        this.analysisRequestProducer = analysisRequestProducer;
    }

    public String createTask(String prompt, String fileId) {

        String taskId = UUID.randomUUID().toString();

        AnalysisRequested request = new AnalysisRequested(
                taskId,
                prompt,
                fileId,
                Instant.now(),
                1
        );

        analysisRequestProducer.sendAnalysisRequest(request);

        return taskId;
    }
}