package com.analysis.service.service;

import com.analysis.service.ai_service.AIService;
import com.analysis.service.event.AnalysisResultProducer;
import com.analysis.service.event.TaskStatusProducer;
import com.event.platform.events.AnalysisRequested;
import com.event.platform.events.AnalysisResult;
import com.event.platform.events.TaskStatus;
import com.event.platform.events.TaskStatusChanged;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisTaskProcessor {

    private final AnalysisResultProducer resultProducer;
    private final TaskStatusProducer statusProducer;
    private final AIService aiService;
    private final DocumentVectorStoreService documentVectorStoreService;

    public void process(AnalysisRequested request) {
        log.info("Processing taskId={} fileId={}", request.getTaskId(), request.getFileId());
        statusProducer.sendStatusChanged(
                new TaskStatusChanged(
                        request.getTaskId(),
                        TaskStatus.PROCESSING,
                        Instant.now()
                )
        );

        String response;

        if (request.getFileId() == null) {

            statusProducer.sendStatusChanged(
                    new TaskStatusChanged(
                            request.getTaskId(),
                            TaskStatus.GENERATING,
                            Instant.now()
                    )
            );

            response = aiService.generate(request.getPrompt());

        } else {

            statusProducer.sendStatusChanged(
                    new TaskStatusChanged(
                            request.getTaskId(),
                            TaskStatus.RETRIEVING_CONTEXT,
                            Instant.now()
                    )
            );

            List<Document> context =
                    documentVectorStoreService.search(
                            request.getPrompt(),
                            request.getFileId()
                    );
            log.debug("Retrieved {} context documents for taskId={}", context.size(), request.getTaskId());
            statusProducer.sendStatusChanged(
                    new TaskStatusChanged(
                            request.getTaskId(),
                            TaskStatus.GENERATING,
                            Instant.now()
                    )
            );

            response = aiService.generate(
                    request.getPrompt(),
                    context
            );
        }

        AnalysisResult result = new AnalysisResult(
                request.getTaskId(),
                TaskStatus.COMPLETED,
                response,
                null,
                Instant.now()
        );

        log.info("Task completed taskId={}", request.getTaskId());
        resultProducer.sendAnalysisResult(result);
    }

    public void failTask(AnalysisRequested task, String error) {
        log.warn("Failing taskId={} error={}", task.getTaskId(), error);

        TaskStatusChanged statusChanged = new TaskStatusChanged(
                task.getTaskId(),
                TaskStatus.FAILED,
                Instant.now()
        );

        statusProducer.sendStatusChanged(statusChanged);

        AnalysisResult result = new AnalysisResult(
                task.getTaskId(),
                TaskStatus.FAILED,
                null,
                error,
                Instant.now()
        );

        resultProducer.sendAnalysisResult(result);
        }
}