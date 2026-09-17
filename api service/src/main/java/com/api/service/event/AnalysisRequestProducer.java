package com.api.service.event;


import com.api.service.config.KafkaTopics;
import com.event.platform.events.AnalysisRequested;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisRequestProducer {

    private final KafkaTemplate<String, AnalysisRequested> kafkaTemplate;

    public void sendAnalysisRequest(AnalysisRequested event) {
        log.debug("Sending analysis request taskId={} fileId={}", event.getTaskId(), event.getFileId());
        kafkaTemplate.send(
                KafkaTopics.ANALYSIS_REQUESTS,
                event.getTaskId(),
                event
        );
    }
}