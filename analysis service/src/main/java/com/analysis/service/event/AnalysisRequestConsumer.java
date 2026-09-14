package com.analysis.service.event;

import com.event.platform.events.AnalysisRequested;
import com.event.platform.events.AnalysisResult;
import com.event.platform.events.TaskStatus;
import com.event.platform.events.TaskStatusChanged;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AnalysisRequestConsumer {

    private final AnalysisResultProducer resultProducer;
    private final TaskStatusProducer statusProducer;

    public AnalysisRequestConsumer(
        AnalysisResultProducer resultProducer,
        TaskStatusProducer statusProducer) {

    this.resultProducer = resultProducer;
    this.statusProducer = statusProducer;
}

    @KafkaListener(
            topics = "analysis-requests",
            groupId = "analysis-workers"
    )
    public void consumeAnalysisRequest(AnalysisRequested request) {

        System.out.println("Received analysis request");
        System.out.println("Task ID: " + request.getTaskId());
        System.out.println("Prompt: " + request.getPrompt());
        System.out.println("Attempt: " + request.getAttempt());

        // Simulate analysis work
        try {
            
            statusProducer.sendStatusChanged(new TaskStatusChanged(request.getTaskId(),TaskStatus.PROCESSING,Instant.now()));
            Thread.sleep(3000);
            statusProducer.sendStatusChanged(new TaskStatusChanged(request.getTaskId(),TaskStatus.GENERATING,Instant.now()));
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        AnalysisResult result = new AnalysisResult(
                request.getTaskId(),
                TaskStatus.COMPLETED,
                "Fake analysis completed for: " + request.getPrompt(),
                null,
                request.getAttempt(),
                Instant.now()
        );

        resultProducer.sendAnalysisResult(result);
    }
}