package com.api.service.event;

import com.api.service.config.KafkaTopics;
import com.event.platform.events.AnalysisResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisResultConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(
        topics = KafkaTopics.ANALYSIS_RESULTS,
        groupId = "api-results"
    )
    public void consumeAnalysisResult(AnalysisResult result) {
        log.info("Received analysis result for taskId={} status={}", result.getTaskId(), result.getStatus());
        log.debug("TaskId: {}, resultLength: {}", result.getTaskId(), result.getResult() != null ? result.getResult().length() : 0);

        if (result.getError() != null) {
            log.warn("TaskId: {} completed with error: {}", result.getTaskId(), result.getError());
        }
        messagingTemplate.convertAndSend(
                "/topic/task-updates",
                result
        );
    }
}