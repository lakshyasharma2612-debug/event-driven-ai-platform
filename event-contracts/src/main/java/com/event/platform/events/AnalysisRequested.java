package com.event.platform.events;

import java.time.Instant;

public class AnalysisRequested {

    private String taskId;
    private String prompt;
    private String fileId;
    private Instant requestedAt;


    public AnalysisRequested() {}

    public AnalysisRequested(String taskId, String prompt, String fileId,
                             Instant requestedAt) {
        this.taskId = taskId;
        this.prompt = prompt;
        this.fileId = fileId;
        this.requestedAt = requestedAt;
  
    }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }

    public Instant getRequestedAt() { return requestedAt; }
    public void setRequestedAt(Instant requestedAt) { this.requestedAt = requestedAt; }
}