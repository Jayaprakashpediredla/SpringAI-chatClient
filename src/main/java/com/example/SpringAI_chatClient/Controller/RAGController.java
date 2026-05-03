package com.example.SpringAI_chatClient.Controller;

import com.example.SpringAI_chatClient.DTO.CustomRAGRequest;
import com.example.SpringAI_chatClient.DTO.DocumentRequest;
import com.example.SpringAI_chatClient.DTO.RAGQuestion;
import com.example.SpringAI_chatClient.Service.RAGService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/rag")
public class RAGController {

    private final RAGService ragService;

    public RAGController(RAGService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/answer")
    public ResponseEntity<String> ragAnswer(@RequestBody RAGQuestion request) {
        String answer = ragService.ragAnswer(request.question());
        return ResponseEntity.ok(answer);
    }

    @PostMapping("/answer-custom")
    public ResponseEntity<String> ragAnswerCustom(@RequestBody CustomRAGRequest request) {
        String answer = ragService.ragAnswerWithCustomContext(
                request.question(),
                request.contextDocuments()
        );
        return ResponseEntity.ok(answer);
    }

    @PostMapping("/add-document")
    public ResponseEntity<String> addDocument(@RequestBody DocumentRequest request) {
        ragService.addDocument(request.documentId(), request.content());
        return ResponseEntity.ok("Document added to knowledge base");
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getKnowledgeBaseStats() {
        Map<String, Object> stats = ragService.getKnowledgeBaseStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/documents")
    public ResponseEntity<Map<String, String>> getAllDocuments() {
        Map<String, String> documents = ragService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearKnowledgeBase() {
        ragService.clearKnowledgeBase();
        return ResponseEntity.ok("Knowledge base cleared");
    }

}
