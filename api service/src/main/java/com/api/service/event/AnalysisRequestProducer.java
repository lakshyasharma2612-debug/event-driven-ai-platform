package com.api.service.event;


import com.event.platform.events.AnalysisRequested;
 import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AnalysisRequestProducer {

    private final KafkaTemplate<String, AnalysisRequested> kafkaTemplate;

    public AnalysisRequestProducer(
            KafkaTemplate<String, AnalysisRequested> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAnalysisRequest(AnalysisRequested event) {
        kafkaTemplate.send(
                "analysis-requests",
                event.getTaskId(),
                event
        );
    }
}