package com.example.SpringAI_chatClient.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class StreamingChatService {

    private final ChatClient chatClient;

    public StreamingChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public Flux<String> streamChatResponse(String userMessage) {
        return chatClient
                .prompt()
                .user(userMessage)
                .stream()
                .content();
    }

    public Flux<String> streamWithSystemPrompt(String systemMessage, String userMessage) {
        return chatClient
                .prompt()
                .system(systemMessage)
                .user(userMessage)
                .stream()
                .content();
    }

    public Flux<String> streamCodeGeneration(String language, String requirements) {
        String systemPrompt = String.format(
                "You are an expert %s developer. Generate clean, well-documented code.",
                language
        );
        String userPrompt = "Write " + language + " code for: " + requirements;

        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .stream()
                .content();
    }

    public String streamAndCollect(String userMessage) {
        return streamChatResponse(userMessage)
                .collectList()
                .map(tokens -> String.join("", tokens))
                .block();
    }
}
