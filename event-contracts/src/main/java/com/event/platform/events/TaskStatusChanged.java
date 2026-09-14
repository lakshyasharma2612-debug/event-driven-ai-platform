package com.event.platform.events;

import java.time.Instant;

public class TaskStatusChanged {
    private String taskId;
    private TaskStatus status;
    private Instant timestamp;

    public TaskStatusChanged() {}

    public TaskStatusChanged(String taskId, TaskStatus status, Instant timestamp) {
        this.taskId = taskId;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
