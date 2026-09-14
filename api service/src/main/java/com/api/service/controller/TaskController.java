package com.api.service.controller;

import com.api.service.service.TaskService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public String createTask(
            @RequestParam String prompt,
            @RequestParam(required = false) String fileId) {

        return taskService.createTask(prompt, fileId);
    }
}