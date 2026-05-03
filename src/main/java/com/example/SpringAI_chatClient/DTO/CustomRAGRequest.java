package com.example.SpringAI_chatClient.DTO;

import java.util.List;

public record CustomRAGRequest(String question, List<String> contextDocuments) {
}
