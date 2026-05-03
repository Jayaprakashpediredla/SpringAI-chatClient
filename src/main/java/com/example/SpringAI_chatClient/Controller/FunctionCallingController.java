package com.example.SpringAI_chatClient.Controller;

import com.example.SpringAI_chatClient.DTO.QueryRequest;
import com.example.SpringAI_chatClient.DTO.TranslateRequest;
import com.example.SpringAI_chatClient.Service.FunctionCallingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/functionCalling")
public class FunctionCallingController {

    private final FunctionCallingService functionCallingService;

    public FunctionCallingController(FunctionCallingService functionCallingService) {
        this.functionCallingService = functionCallingService;
    }

    @GetMapping("/weather")
    public ResponseEntity<String> getWeather(@RequestParam String location) {
        String weather = functionCallingService.getWeather(location);
        return ResponseEntity.ok(weather);
    }

    @GetMapping("/calculate")
    public ResponseEntity<Integer> calculate(@RequestParam String expression) {
        int result = functionCallingService.calculate(expression);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/time")
    public ResponseEntity<String> getCurrentTime() {
        String time = functionCallingService.getCurrentTime();
        return ResponseEntity.ok(time);
    }

    @PostMapping("/translate")
    public ResponseEntity<String> translate(@RequestBody TranslateRequest request) {
        String translation = functionCallingService.translate(
                request.text(),
                request.targetLanguage()
        );
        return ResponseEntity.ok(translation);
    }

    @PostMapping("/query-with-tools")
    public ResponseEntity<String> queryWithTools(@RequestBody QueryRequest request) {
        String response = functionCallingService.processQueryWithTools(request.query());
        return ResponseEntity.ok(response);
    }
}
