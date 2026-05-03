package com.example.SpringAI_chatClient.Controller;

import com.example.SpringAI_chatClient.DTO.ChatRequest;
import com.example.SpringAI_chatClient.DTO.ChatResponseDTO;
import com.example.SpringAI_chatClient.DTO.TemplateRequest;
import com.example.SpringAI_chatClient.Service.ExpertChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class BasicChatController {

    private final ChatClient chatClient;
    private final ExpertChatService expertChatService;

    // Spring AI automatically creates a ChatClient.Builder based on your properties
    public BasicChatController(ChatClient.Builder builder, ExpertChatService expertChatService) {
        this.chatClient = builder.build();
        this.expertChatService = expertChatService;
    }

    @GetMapping("/simple-chat")
    public String chat(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @PostMapping("/chat-with-system")
    public ResponseEntity<ChatResponseDTO> chat(@RequestBody ChatRequest request) {

        String response = chatClient.prompt()
                .system(request.systemMessage())
                .user(request.userMessage())
                .call()
                .content();

        // Wrapping in a DTO so the response is also structured JSON
        return ResponseEntity.ok(new ChatResponseDTO(response, "llama3"));
    }

    @PostMapping("/chat-template")
    public ResponseEntity<ChatResponseDTO> chatWithTemplate(@RequestBody TemplateRequest request) {

        // 1. Define the template with placeholders in {curly_brackets}
        String template = """
            Write an explanation about {topic} in maximum {wordLimit} words.
            Make it beginner-friendly and easy to understand.
            """;

        // 2. Create the template and map the data from the request DTO
        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of(
                "topic", request.topic(),
                "wordLimit", request.wordLimit()
        ));

        // 3. Pass the generated prompt to the chatClient
        String response = chatClient
                .prompt(prompt)
                .call()
                .content();

        return ResponseEntity.ok(new ChatResponseDTO(response, "llama3"));
    }

    @GetMapping("/expert-chat")
    public ResponseEntity<String> expertChat(
            @RequestParam String expertise,
            @RequestParam String question) {
        String response = expertChatService.chatAsExpert(expertise, question);
        return ResponseEntity.ok(response);
    }
}
