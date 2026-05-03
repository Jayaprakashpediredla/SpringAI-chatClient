package com.example.SpringAI_chatClient.Controller;

import com.example.SpringAI_chatClient.DTO.EmbeddingRequest;
import com.example.SpringAI_chatClient.DTO.SimilarDocument;
import com.example.SpringAI_chatClient.DTO.StoreDocumentRequest;
import com.example.SpringAI_chatClient.Service.EmbeddingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/embeddings")
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    public EmbeddingController(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @PostMapping("/generate")
    public ResponseEntity<List<Double>> generateEmbedding(@RequestBody EmbeddingRequest request) {
        List<Double> embedding = embeddingService.generateEmbedding(request.text());
        return ResponseEntity.ok(embedding);
    }

    @PostMapping("/store")
    public ResponseEntity<String> storeDocument(@RequestBody StoreDocumentRequest request) {
        embeddingService.storeDocument(request.documentId(), request.content());
        return ResponseEntity.ok("Document stored successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<String>> semanticSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK) {
        System.out.println("Query param value: "+query);
        List<String> results = embeddingService.semanticSearch(query, topK);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/similar")
    public ResponseEntity<List<SimilarDocument>> findSimilarDocuments(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK) {
        List<SimilarDocument> results = embeddingService.findSimilarDocuments(query, topK);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/documents")
    public ResponseEntity<List<String>> getAllDocuments() {
        List<String> documents = embeddingService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearEmbeddings() {
        embeddingService.clearEmbeddings();
        return ResponseEntity.ok("All embeddings cleared");
    }

    @GetMapping("/dimension")
    public ResponseEntity<Integer> getEmbeddingDimension() {
        int dimension = embeddingService.getEmbeddingDimension();
        return ResponseEntity.ok(dimension);
    }

}
