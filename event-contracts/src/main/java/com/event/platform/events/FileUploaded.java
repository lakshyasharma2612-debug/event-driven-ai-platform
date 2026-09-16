package com.event.platform.events;

import java.time.Instant;

public class FileUploaded {

    private String fileId;
    private String contentType;
    private Instant uploadedAt;

    public FileUploaded() {
    }

    public FileUploaded(String fileId, String contentType, Instant uploadedAt) {
        this.fileId = fileId;
        this.contentType = contentType;
        this.uploadedAt = uploadedAt;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}