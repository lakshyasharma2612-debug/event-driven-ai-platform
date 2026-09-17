package com.analysis.service.event;

import com.analysis.config.KafkaTopics;
import com.analysis.service.service.AnalysisTaskProcessor;
import com.analysis.service.service.DocumentStateManager;
import com.event.platform.events.AnalysisRequested;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisRequestConsumer {

    private final DocumentStateManager stateManager;
    private final AnalysisTaskProcessor taskProcessor;

    @KafkaListener(
        topics = KafkaTopics.ANALYSIS_REQUESTS,
        groupId = "analysis-workers"
        )
    public void consumeAnalysisRequest(AnalysisRequested request) {
        log.info("Received analysis request taskId={} fileId={}", request.getTaskId(), request.getFileId());

        if (request.getFileId() == null) {
            taskProcessor.process(request);
            return;
        }

        boolean waiting = stateManager.addIfNotReady(request);

        if (waiting) {
            log.info("Document not ready. Task waiting: {}", request.getTaskId());
            return;
        }

        taskProcessor.process(request);
    }
}