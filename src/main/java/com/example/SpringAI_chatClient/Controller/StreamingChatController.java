package com.example.SpringAI_chatClient.Controller;

import com.example.SpringAI_chatClient.DTO.CodeGenRequest;
import com.example.SpringAI_chatClient.DTO.StreamChatRequest;
import com.example.SpringAI_chatClient.DTO.SystemStreamRequest;
import com.example.SpringAI_chatClient.Service.StreamingChatService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * REST Controller for Module 2: Streaming Responses
 *
 * Endpoints:
 * - POST /api/v1/stream/chat (returns Server-Sent Events)
 * - POST /api/v1/stream/chat-with-system
 * - POST /api/v1/stream/code
 * - POST /api/v1/stream/collect
 */
@RestController
@RequestMapping("/api/v1/stream")
public class StreamingChatController {
    private final StreamingChatService streamingChatService;

    public StreamingChatController(StreamingChatService streamingChatService) {
        this.streamingChatService = streamingChatService;
    }

    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestBody StreamChatRequest request) {
        return streamingChatService.streamChatResponse(request.message())
                .map(token -> "data: " + token + "\n\n");
    }

    @PostMapping(value = "/chat-with-system", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamWithSystem(@RequestBody SystemStreamRequest request) {
        return streamingChatService.streamWithSystemPrompt(
                request.systemMessage(),
                request.userMessage()
        ).map(token -> "data: " + token + "\n\n");
    }

    @PostMapping(value = "/code", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamCode(@RequestBody CodeGenRequest request) {
        return streamingChatService.streamCodeGeneration(
                request.language(),
                request.requirements()
        ).map(token -> "data: " + token + "\n\n");
    }

    /*
     * Collect all streamed content and return as single response
     */
    @PostMapping("/collect")
    public ResponseEntity<String> collectStream(@RequestBody StreamChatRequest request) {
        String response = streamingChatService.streamAndCollect(request.message());
        return ResponseEntity.ok(response);
    }
}
