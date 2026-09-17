package com.api.service.controller;

import com.api.service.dto.CreateTaskRequest;
import com.api.service.dto.CreateTaskResponse;
import com.api.service.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<CreateTaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request) {
        log.debug("Received createTask request fileId={}", request.getFileId());
        String taskId = taskService.createTask(
                request.getPrompt(),
                request.getFileId()
        );

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new CreateTaskResponse(taskId));
        }
}