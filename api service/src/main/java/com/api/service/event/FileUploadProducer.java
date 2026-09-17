package com.api.service.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.api.service.config.KafkaTopics;
import com.event.platform.events.FileUploaded;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadProducer {
    private final KafkaTemplate<String, FileUploaded> kafkaTemplate;

    public void sendFileUploaded(FileUploaded file) {
        log.debug("Sending file uploaded event fileId={} contentType={}", file.getFileId(), file.getContentType());
        kafkaTemplate.send(KafkaTopics.FILE_UPLOADED, file.getFileId(), file);
    }

    /** @deprecated use {@link #sendFileUploaded(FileUploaded)} - typo method kept for backwards compatibility */
    @Deprecated
    public void sebdFileupload(FileUploaded file) {
        sendFileUploaded(file);
    }
}
