package com.api.service.controller;

import java.time.Instant;
import java.util.UUID;

import com.api.service.event.AnalysisRequestProducer;
import com.event.platform.events.AnalysisRequested;
 import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/test")
@RequiredArgsConstructor 
public class controller {
    private final AnalysisRequestProducer analysisProducer;

    @PostMapping 
    public String test(@RequestBody String prompt){
        String taskId = UUID.randomUUID().toString();

        AnalysisRequested event = new AnalysisRequested(
                taskId,
                prompt,
                null,
                Instant.now(),
                1
        );

        analysisProducer.sendAnalysisRequest(event);

        return taskId;
    }
}
