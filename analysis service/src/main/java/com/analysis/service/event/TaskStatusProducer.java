package com.analysis.service.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.analysis.config.KafkaTopics;
import com.event.platform.events.TaskStatusChanged;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskStatusProducer {
    private final KafkaTemplate<String, TaskStatusChanged> kafkaTemplate;

    public void sendStatusChanged(TaskStatusChanged taskStatusChanged){
        log.debug("Sending task status change taskId={} status={}", taskStatusChanged.getTaskId(), taskStatusChanged.getStatus());
        kafkaTemplate.send(KafkaTopics.TASK_STATUS,taskStatusChanged.getTaskId(),taskStatusChanged);
    }
}
