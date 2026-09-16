package com.analysis.service.event;

import com.analysis.service.ai_service.AIService;
import com.event.platform.events.AnalysisRequested;
import com.event.platform.events.AnalysisResult;
import com.event.platform.events.TaskStatus;
import com.event.platform.events.TaskStatusChanged;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
 

@Service
@RequiredArgsConstructor
public class AnalysisRequestConsumer {

    private final AnalysisResultProducer resultProducer;
    private final TaskStatusProducer statusProducer;
    private final AIService aiService;
    private final DocumentStateManager stateManager;

    @KafkaListener(
            topics = "analysis-requests",
            groupId = "analysis-workers"
    )
    public void consumeAnalysisRequest(AnalysisRequested request) {

        System.out.println("Received analysis request");
        System.out.println("Task ID: " + request.getTaskId());
        System.out.println("Prompt: " + request.getPrompt());

        try {

            // No document required
            if (request.getFileId() == null) {
                processTask(request);
                return;
            }

            // Document required
            boolean waiting = stateManager.addIfNotReady(request);

            if (waiting) {
                System.out.println(
                        "Document not ready. Task waiting: "
                                + request.getTaskId()
                );
                return;
            }

            // Document is already ready
            processTask(request);

        } catch (Exception e) {

            handleFailure(request, e);
        }
    }

    public void processTask(AnalysisRequested request) {

        statusProducer.sendStatusChanged(
                new TaskStatusChanged(
                        request.getTaskId(),
                        TaskStatus.PROCESSING,
                        Instant.now()
                )
        );

        statusProducer.sendStatusChanged(
                new TaskStatusChanged(
                        request.getTaskId(),
                        TaskStatus.GENERATING,
                        Instant.now()
                )
        );

        String response = aiService.generate(request.getPrompt());

        AnalysisResult result = new AnalysisResult(
                request.getTaskId(),
                TaskStatus.COMPLETED,
                response,
                null,
                Instant.now()
        );

        resultProducer.sendAnalysisResult(result);
    }

    private void handleFailure(
            AnalysisRequested request,
            Exception e
    ) {

        System.err.println(
                "AI generation failed for task "
                        + request.getTaskId()
        );

        e.printStackTrace();

        statusProducer.sendStatusChanged(
                new TaskStatusChanged(
                        request.getTaskId(),
                        TaskStatus.FAILED,
                        Instant.now()
                )
        );

        AnalysisResult result = new AnalysisResult(
                request.getTaskId(),
                TaskStatus.FAILED,
                null,
                "AI generation failed. Please try again later.",
                Instant.now()
        );

        resultProducer.sendAnalysisResult(result);
    }
}