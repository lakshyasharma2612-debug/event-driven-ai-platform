package com.event.platform.events;

import java.time.Instant;

public class AnalysisResult {

    private String taskId;
    private TaskStatus status;
    private String result;
    private String error;
    private int attempt;
    private Instant completedAt;

    public AnalysisResult() {}

    public AnalysisResult(String taskId, TaskStatus status, String result,
                          String error, int attempt, Instant completedAt) {
        this.taskId = taskId;
        this.status = status;
        this.result = result;
        this.error = error;
        this.attempt = attempt;
        this.completedAt = completedAt;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }
}