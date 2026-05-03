package com.example.SpringAI_chatClient.exception;

import com.example.SpringAI_chatClient.DTO.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. CATCH CONNECTION ERRORS (Ollama is offline)
    @ExceptionHandler(org.springframework.web.client.ResourceAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleConnectionError(Exception e) {
        return buildResponse(
                "AI Service (Ollama) is unreachable. Ensure it is running on port 11434.",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    // 2. CATCH BAD JSON / MISSING FIELDS (Client Error)
    @ExceptionHandler({
            org.springframework.web.bind.MethodArgumentNotValidException.class,
            org.springframework.http.converter.HttpMessageNotReadableException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(Exception e) {
        // We use the actual error message here because it helps the developer fix the JSON
        String message = (e instanceof IllegalArgumentException) ? e.getMessage() : "Invalid request format or missing fields.";
        return buildResponse(message, HttpStatus.BAD_REQUEST);
    }

    // 3. CATCH-ALL FOR EVERYTHING ELSE (Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralError(Exception e) {
        // Log the actual error internally so you can debug, but show a generic message to the user
        e.printStackTrace();
        return buildResponse(
                "An unexpected internal error occurred.",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(org.springframework.web.servlet.NoHandlerFoundException e) {
        return buildResponse(
                "The requested endpoint does not exist. Please check the URL.",
                HttpStatus.NOT_FOUND
        );
    }


    // Helper method to keep code clean (DRY - Don't Repeat Yourself)
    private ResponseEntity<ErrorResponseDTO> buildResponse(String message, HttpStatus status) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                message,
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }
}

