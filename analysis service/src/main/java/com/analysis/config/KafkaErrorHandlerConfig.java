package com.analysis.config;

import com.analysis.service.service.AnalysisTaskProcessor;
import com.event.platform.events.AnalysisRequested;

 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorHandlerConfig {

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(
            KafkaTemplate<String, Object> kafkaTemplate,
            AnalysisTaskProcessor taskProcessor) {

        DeadLetterPublishingRecoverer dltRecoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate);

        ConsumerRecordRecoverer recoverer = (record, exception) -> {

            Object value = record.value();

            if (value instanceof AnalysisRequested request) {

                String errorMessage = exception.getMessage();

                if (errorMessage == null || errorMessage.isBlank()) {
                    errorMessage = exception.getClass().getSimpleName();
                }

                taskProcessor.failTask(
                        request,
                        "Kafka processing failed: " + errorMessage
                );
            }

 
            dltRecoverer.accept(record, exception);
        };

        FixedBackOff backOff = new FixedBackOff(
                2000L,
                2L
        );

        return new DefaultErrorHandler(
                recoverer,
                backOff
        );
    }
}