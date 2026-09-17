package com.analysis.service.event;

import com.analysis.config.KafkaTopics;
import com.event.platform.events.AnalysisResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisResultProducer {

    private final KafkaTemplate<String, AnalysisResult> kafkaTemplate;

    public void sendAnalysisResult(AnalysisResult result) {
        log.debug("Sending analysis result taskId={} status={}", result.getTaskId(), result.getStatus());
        kafkaTemplate.send(
                KafkaTopics.ANALYSIS_RESULTS,
                result.getTaskId(),
                result
        );
    }
}