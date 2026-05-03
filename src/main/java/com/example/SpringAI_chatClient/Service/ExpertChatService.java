package com.example.SpringAI_chatClient.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ExpertChatService {

    private final ChatClient chatClient;

    public ExpertChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String chatAsExpert(String expertise, String question) {
        String systemPrompt = String.format(
                "You are an expert in %s. Please answer the following question thoroughly but concisely.",
                expertise
        );

        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(question)
                .call()
                .content();
    }
}
