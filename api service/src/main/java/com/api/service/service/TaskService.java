package com.api.service.service;

import com.api.service.event.AnalysisRequestProducer;
import com.event.platform.events.AnalysisRequested;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final AnalysisRequestProducer analysisRequestProducer;

    public String createTask(String prompt, String fileId) {
        log.info("Creating task fileId={} promptLength={}", fileId, prompt != null ? prompt.length() : 0);

        String taskId = UUID.randomUUID().toString();

        AnalysisRequested request = new AnalysisRequested(
                taskId,
                prompt,
                fileId,
                Instant.now()      
        );

        analysisRequestProducer.sendAnalysisRequest(request);

        return taskId;
    }
}