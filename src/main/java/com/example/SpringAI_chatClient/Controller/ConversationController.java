package com.example.SpringAI_chatClient.Controller;

import com.example.SpringAI_chatClient.DTO.ChatMessage;
import com.example.SpringAI_chatClient.DTO.StartConversationRequest;
import com.example.SpringAI_chatClient.Service.ConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conversation")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody ChatMessage message){
        String response = conversationService.chat(message.message());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/start")
    public ResponseEntity<String> startConversation(@RequestBody StartConversationRequest request) {
        String response = conversationService.startConversationWithContext(
                request.context(),
                request.firstMessage()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<String> getConversationSummary() {
        String summary = conversationService.getConversationSummary();
        return ResponseEntity.ok(summary);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearConversation() {
        conversationService.clearConversation();
        return ResponseEntity.ok("Conversation cleared successfully");
    }

    @GetMapping("/history")
    public ResponseEntity<List<String>> getConversationHistory() {
        List<String> history = conversationService.getConversationHistory();
        return ResponseEntity.ok(history);
    }

}
