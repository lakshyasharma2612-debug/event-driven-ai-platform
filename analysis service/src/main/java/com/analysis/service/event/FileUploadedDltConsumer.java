package com.analysis.service.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.analysis.config.KafkaTopics;
import com.analysis.service.service.AnalysisTaskProcessor;
import com.analysis.service.service.DocumentStateManager;
import com.event.platform.events.FileUploaded;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadedDltConsumer {

    private final DocumentStateManager documentStateManager;
    private final AnalysisTaskProcessor analysisTaskProcessor;

    @KafkaListener(
             topics = KafkaTopics.FILE_UPLOADED_DLT,
            groupId = "document-ingestion-dlt"
    )
    public void consume(FileUploaded event) {

       var waitingTasks =
        documentStateManager.documentFailed(event.getFileId());

        log.warn(
                "DLT received for fileId={} - failing {} waiting tasks",
                event.getFileId(),
                waitingTasks.size()
        );
        for (var task : waitingTasks) {
            analysisTaskProcessor.failTask(
                    task,
                    "Document ingestion failed"
            );
        }
    }
}