package com.example.SpringAI_chatClient.Service;

import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FunctionCallingService {

    private final ChatClient chatClient;

    public FunctionCallingService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String getWeather(String location) {

        return String.format("Weather in %s: Sunny, 72°F", location);

        /*// In real world, this would call an actual weather API. Below is a pseudo-code example of how you might implement this:

        // 1. Define the API details (usually stored in application.properties)
        String apiKey = "your_api_key_here";
        String url = "https://openweathermap.org" + location + "&appid=" + apiKey;

        // 2. Make the actual network call
        WeatherResponse response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(WeatherResponse.class); // Map JSON to a Java object

        // 3. Extract the data
        return String.format("Current weather in %s: %s, %.1f°C",
                    location,
                    response.weather()[0].description(),
                    response.main().temp());*/
    }

    public int calculate(String expression) {
        try {
            // This library works on all Java versions and is safe from injection
            return (int) new ExpressionBuilder(expression)
                    .build()
                    .evaluate();
        } catch (Exception e) {
            // Log the error so you know WHY it failed
            System.err.println("Math error: " + e.getMessage());
            return 0;
        }
    }

    public String getCurrentTime() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String translate(String text, String targetLanguage) {
        return String.format("Translation of '%s' to %s: [translated text]", text, targetLanguage);

        /*// In real world, this would call an actual translation API. Below is a pseudo-code example of how you might implement this:

        // 1. Define the API details (usually stored in application.properties)
        String apiKey = "your_api_key_here";
        String url = "https://translation.api/translate?text=" + text + "&target=" + targetLanguage + "&key=" + apiKey;

        // 2. Make the actual network call
        TranslationResponse response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(TranslationResponse.class); // Map JSON to a Java object

        // 3. Extract the translated text
        return response.translatedText();*/
    }

    public String processQueryWithTools(String query) {
        return chatClient
                .prompt()
                .user(String.format(
                        "Answer the following query. You have access to: weather, calculator, time, and translation tools. Query: %s",
                        query
                ))
                .call()
                .content();
    }

}
