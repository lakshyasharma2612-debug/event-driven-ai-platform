package com.analysis.service.event;

import com.event.platform.events.AnalysisResult;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AnalysisResultProducer {

    private final KafkaTemplate<String, AnalysisResult> kafkaTemplate;

    public AnalysisResultProducer(
            KafkaTemplate<String, AnalysisResult> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAnalysisResult(AnalysisResult result) {
        kafkaTemplate.send(
                "analysis-results",
                result.getTaskId(),
                result
        );
    }
}