package com.example.SpringAI_chatClient.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RAGService {

    private final ChatClient chatClient;
    private final Map<String, String> knowledgeBase = new HashMap<>();
    public RAGService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    /**
     * Initialize sample knowledge base
     */
    private void initializeKnowledgeBase() {
        knowledgeBase.put("java-basics",
                "Java is an object-oriented programming language created by Sun Microsystems. " +
                        "It follows the principle of 'write once, run anywhere' (WORA). " +
                        "Java is platform-independent and widely used for enterprise applications.");

        knowledgeBase.put("spring-framework",
                "Spring Framework is a powerful open-source Java framework for building enterprise applications. " +
                        "It provides comprehensive support for building applications with dependency injection, AOP, and more.");

        knowledgeBase.put("spring-ai",
                "Spring AI provides Spring developers with a familiar and convenient API for developing AI applications. " +
                        "It integrates with various AI providers like OpenAI, Claude, and others.");
    }
    /**
     * Retrieve relevant context from knowledge base
     */
    private String retrieveContext(String query) {
        String relevantDocs = "";
        for (Map.Entry<String, String> entry : knowledgeBase.entrySet()) {
            if (entry.getValue().toLowerCase().contains(query.toLowerCase())) {
                relevantDocs += entry.getValue() + "\n\n";
            }
        }
        return relevantDocs.isEmpty() ? "No relevant context found." : relevantDocs;
    }
    /**
     * RAG response - Retrieves context then generates answer
     */
    public String ragAnswer(String question) {
        // Step 1: Retrieve relevant context
        String context = retrieveContext(question);

        // Step 2: Build augmented prompt
        String systemPrompt = "You are a helpful AI assistant. Use the provided context to answer the question. " +
                "If the context doesn't contain relevant information, say so.";

        String augmentedPrompt = String.format(
                "Context:\n%s\n\nQuestion: %s",
                context,
                question
        );

        // Step 3: Generate response using augmented prompt
        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(augmentedPrompt)
                .call()
                .content();
    }
    /**
     * RAG with custom knowledge base
     */
    public String ragAnswerWithCustomContext(String question, List<String> contextDocuments) {
        String context = String.join("\n\n", contextDocuments);

        String augmentedPrompt = String.format(
                "Based on the following context:\n\n%s\n\nAnswer: %s",
                context,
                question
        );

        return chatClient
                .prompt()
                .user(augmentedPrompt)
                .call()
                .content();
    }
    /**
     * Add document to knowledge base
     */
    public void addDocument(String documentId, String content) {
        knowledgeBase.put(documentId, content);
    }
    /**
     * Get knowledge base statistics
     */
    public Map<String, Object> getKnowledgeBaseStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDocuments", knowledgeBase.size());
        stats.put("documents", knowledgeBase.keySet());
        stats.put("totalCharacters", knowledgeBase.values().stream()
                .mapToLong(String::length)
                .sum());
        return stats;
    }
    /**
     * Get all documents
     */
    public Map<String, String> getAllDocuments() {
        return new HashMap<>(knowledgeBase);
    }

    /**
     * Clear knowledge base
     */
    public void clearKnowledgeBase() {
        knowledgeBase.clear();
    }

}
