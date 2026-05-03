package com.example.SpringAI_chatClient.Service;

import com.example.SpringAI_chatClient.DTO.SimilarDocument;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.ai.vectorstore.SimpleVectorStore.EmbeddingMath.cosineSimilarity;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;
    private final Map<String, List<Double>> documentEmbeddings = new HashMap<>();

    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public List<Double> generateEmbedding(String text) {
        return embeddingModel.embed(text);
    }
    /**
     * Store document embedding for later similarity search
     */
    public void storeDocument(String documentId, String content) {
        List<Double> embedding = generateEmbedding(content);
        documentEmbeddings.put(documentId, embedding);
    }
    /**
     * Semantic search - find documents by meaning, not keywords
     */
    public List<String> semanticSearch(String query, int topK) {
        return findSimilarDocuments(query, topK).stream()
                .map(SimilarDocument::documentId)
                .toList();
    }
    public List<SimilarDocument> findSimilarDocuments(String query, int topK) {
        List<Double> queryEmbedding = generateEmbedding(query);

        return documentEmbeddings.entrySet().stream()
                .map(entry -> new SimilarDocument(
                        entry.getKey(),
                        cosineSimilarity(queryEmbedding, entry.getValue())
                ))
                .sorted((a, b) -> Double.compare(b.similarity(), a.similarity()))
                .limit(topK)
                .toList();
    }
    /**
     * Get all stored documents
     */
    public List<String> getAllDocuments() {
        return new ArrayList<>(documentEmbeddings.keySet());
    }

    /**
     * Clear all stored embeddings
     */
    public void clearEmbeddings() {
        documentEmbeddings.clear();
    }

    /**
     * Get embedding dimension
     */
    public int getEmbeddingDimension() {
        if (documentEmbeddings.isEmpty()) {
            return generateEmbedding("test").size();
        }
        return documentEmbeddings.values().iterator().next().size();
    }


}
