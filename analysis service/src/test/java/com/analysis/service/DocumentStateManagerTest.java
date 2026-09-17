package com.analysis.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.analysis.service.service.DocumentStateManager;
import com.analysis.service.service.DocumentVectorStoreService;
import com.event.platform.events.AnalysisRequested;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DocumentStateManagerTest {

    private DocumentVectorStoreService vectorStoreService;
    private DocumentStateManager stateManager;

    @BeforeEach
    void setUp() {
        vectorStoreService = mock(DocumentVectorStoreService.class);
        stateManager = new DocumentStateManager(vectorStoreService);
    }

    @Test
    void shouldWaitWhenDocumentIsNotReady() {
        AnalysisRequested task = new AnalysisRequested(
                "task-1",
                "Summarize",
                "file-1",
                Instant.now()
        );

        when(vectorStoreService.isDocumentReady("file-1"))
                .thenReturn(false);

        boolean waiting = stateManager.addIfNotReady(task);

        assertTrue(waiting);
    }

    @Test
    void shouldNotWaitWhenDocumentIsReady() {
        AnalysisRequested task = new AnalysisRequested(
                "task-1",
                "Summarize",
                "file-1",
                Instant.now()
        );

        when(vectorStoreService.isDocumentReady("file-1"))
                .thenReturn(true);

        boolean waiting = stateManager.addIfNotReady(task);

        assertFalse(waiting);
    }

    @Test
    void shouldReleaseWaitingTasksWhenDocumentIsReady() {
        AnalysisRequested task = new AnalysisRequested(
                "task-1",
                "Summarize",
                "file-1",
                Instant.now()
        );

        when(vectorStoreService.isDocumentReady("file-1"))
                .thenReturn(false);

        stateManager.addIfNotReady(task);

        List<AnalysisRequested> released =
                stateManager.documentReady("file-1");

        assertEquals(1, released.size());
        assertEquals("task-1", released.get(0).getTaskId());
    }

    @Test
    void shouldReleaseWaitingTasksWhenDocumentFails() {
        AnalysisRequested task = new AnalysisRequested(
                "task-1",
                "Summarize",
                "file-1",
                Instant.now()
        );

        when(vectorStoreService.isDocumentReady("file-1"))
                .thenReturn(false);

        stateManager.addIfNotReady(task);

        List<AnalysisRequested> failed =
                stateManager.documentFailed("file-1");

        assertEquals(1, failed.size());
        assertEquals("task-1", failed.get(0).getTaskId());
    }
}