package com.analysis.service.event;

import com.analysis.config.KafkaTopics;
import com.analysis.service.service.DocumentProcessingService;
import com.event.platform.events.FileUploaded;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadedConsumer {

    private final DocumentProcessingService documentProcessingService;

    @KafkaListener(
        topics = KafkaTopics.FILE_UPLOADED,
        groupId = "document-ingestion"
        )
    public void consume(FileUploaded event) throws Exception {
        log.info("File uploaded event received fileId={} contentType={}", event.getFileId(), event.getContentType());

        documentProcessingService.process(event);
    }
}