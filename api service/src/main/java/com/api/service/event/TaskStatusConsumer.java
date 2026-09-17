package com.api.service.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.api.service.config.KafkaTopics;
import com.event.platform.events.TaskStatusChanged;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskStatusConsumer {
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(
        topics = KafkaTopics.TASK_STATUS,
        groupId = "api-status"
    )
    public void consumeStatusChanged(TaskStatusChanged result)
    {
        log.info("Received task status change taskId={} status={} timestamp={}", result.getTaskId(), result.getStatus(), result.getTimestamp());
        messagingTemplate.convertAndSend(
            "/topic/task-updates",
            result
        );
    }
}
