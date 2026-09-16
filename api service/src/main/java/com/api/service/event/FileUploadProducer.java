package com.api.service.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.event.platform.events.FileUploaded;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class FileUploadProducer {
    private  final KafkaTemplate <String,FileUploaded> kafkaTemplate;
    public void sebdFileupload(FileUploaded file){
        kafkaTemplate.send("file-uploaded",file.getFileId(),file);

    }
}
