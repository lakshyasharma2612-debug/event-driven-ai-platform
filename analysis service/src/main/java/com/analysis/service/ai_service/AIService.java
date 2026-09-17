package com.analysis.service.ai_service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final ChatClient chatClient;

    public AIService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generate(String prompt) {

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }

    public String generate(String prompt, List<Document> documents) {

            String context = documents.stream()
                    .map(Document::getText)
                    .collect(Collectors.joining("\n\n"));

            String augmentedPrompt = """
            You are answering a user's question using retrieved document context.

            Rules:
            1. Answer using only the information contained in the document context.
            2. Do not use outside knowledge or make up information.
            3. If the document context does not contain enough information to answer
               the question, clearly say that the answer is not available in the document.
            4. Keep the answer directly relevant to the user's question.

            DOCUMENT CONTEXT:
            ----------------
            %s
            ----------------

            USER QUESTION:
            %s
            """.formatted(context, prompt);

            return chatClient
                    .prompt()
                    .user(augmentedPrompt)
                    .call()
                    .content();
        }
}