package com.analysis.service.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.event.platform.events.TaskStatusChanged;

@Service 
public class TaskStatusProducer {
private final KafkaTemplate<String, TaskStatusChanged> kafkaTemplate;

    public TaskStatusProducer(
            KafkaTemplate<String, TaskStatusChanged> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendStatusChanged(TaskStatusChanged taskStatusChanged){
        kafkaTemplate.send("task-status",taskStatusChanged.getTaskId(),taskStatusChanged);
    }
}
