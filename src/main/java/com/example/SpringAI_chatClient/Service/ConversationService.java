package com.example.SpringAI_chatClient.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationService {

    private static final String CONVERSATION_ID = "default-conversation";
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public ConversationService(ChatClient.Builder builder) {
        this.chatMemory = new InMemoryChatMemory();
        this.chatClient = builder
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory, CONVERSATION_ID, 10)
                )
                .build();
    }

    public String chat(String userMessage) {
        return chatClient
                .prompt()
                .user(userMessage)
                .call()
                .content();
    }

    /**
     * Start a new conversation with context
     */
    public String startConversationWithContext(String systemContext, String firstMessage) {
        return chatClient
                .prompt()
                .system(systemContext)
                .user(firstMessage)
                .call()
                .content();
    }

    /**
     * Get conversation summary
     */
    public String getConversationSummary() {
        return chatClient
                .prompt()
                .user("Please provide a brief summary of our conversation so far.")
                .call()
                .content();
    }

    /**
     * Clear conversation history
     */
    public void clearConversation() {
        chatMemory.clear(CONVERSATION_ID);
    }

    /**
     * Get current conversation history (if available)
     */
    public List<String> getConversationHistory() {
        // This would depend on your ChatMemory implementation
        List<String> history = new ArrayList<>();
        history.add("Conversation history tracking would be implemented based on ChatMemory storage");
        return history;
    }


}
