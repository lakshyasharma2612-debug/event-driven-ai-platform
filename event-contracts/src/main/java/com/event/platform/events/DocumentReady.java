package com.event.platform.events;

import java.time.Instant;

public class DocumentReady {

    private String fileId;
    private Instant readyAt;

    public DocumentReady() {
    }

    public DocumentReady(String fileId, Instant readyAt) {
        this.fileId = fileId;
        this.readyAt = readyAt;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Instant getReadyAt() {
        return readyAt;
    }

    public void setReadyAt(Instant readyAt) {
        this.readyAt = readyAt;
    }
}