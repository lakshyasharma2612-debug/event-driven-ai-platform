package com.event.platform.events;

public enum TaskStatus {
    QUEUED,
    PROCESSING,
    RETRIEVING_CONTEXT,
    GENERATING,
    COMPLETED,
    FAILED
}