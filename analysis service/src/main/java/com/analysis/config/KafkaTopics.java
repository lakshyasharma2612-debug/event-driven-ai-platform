package com.analysis.config;


public final class KafkaTopics {

    private KafkaTopics() {}

    public static final String ANALYSIS_REQUESTS = "analysis-requests";
    public static final String ANALYSIS_RESULTS = "analysis-results";
    public static final String TASK_STATUS = "task-status";
    public static final String FILE_UPLOADED = "file-uploaded";
    public static final String FILE_UPLOADED_DLT = FILE_UPLOADED + ".DLT";
}