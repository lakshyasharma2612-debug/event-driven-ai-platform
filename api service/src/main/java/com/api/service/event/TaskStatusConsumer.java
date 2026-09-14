package com.api.service.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.event.platform.events.TaskStatusChanged;

@Service 
public class TaskStatusConsumer {
    @KafkaListener (topics="task-status",groupId = "api-services")
    public void consumeStatusChanged(TaskStatusChanged result)
    {
        System.out.println("Task ID : "+result.getTaskId());
        System.out.println("Task ID : "+result.getStatus());
        System.out.println("Task ID : "+result.getTimestamp());

    }
}
