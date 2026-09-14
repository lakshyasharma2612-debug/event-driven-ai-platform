package com.api.service.event;



 import com.event.platform.events.AnalysisResult;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AnalysisResultConsumer<AnalysisResultls> {

    @KafkaListener(
            topics = "analysis-results",
            groupId = "api-services"
    )
    public void consumeAnalysisResult(AnalysisResult result) {

        System.out.println("Received analysis result");
        System.out.println("Task ID: " + result.getTaskId());
        System.out.println("Status: " + result.getStatus());
        System.out.println("Result: " + result.getResult());
        System.out.println("Attempt: " + result.getAttempt());

        if (result.getError() != null) {
            System.out.println("Error: " + result.getError());
        }
    }
}