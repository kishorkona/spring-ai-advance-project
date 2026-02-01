package com.work.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class MyOllamaServices {


    private ChatClient chatClient;

    @Async
    public CompletableFuture<String> getWelcomeMessage() {
        System.out.println("Execute method asynchronously. " + Thread.currentThread().getName());
        Random random = new Random();
        return CompletableFuture.completedFuture("Welcome to Spring AI Project: " + random.nextInt(1000));
    }

    public String getPopulation(String countryName) {
        if(!countryName.isBlank() &&  !countryName.isEmpty()) {
            String promptText = "What is the current population of "+countryName+"? Respond with only the number, with no additional text, words, or formatting.";
            System.out.println("Prompt Text: " + promptText);
            OllamaOptions options = OllamaOptions.builder()
                    .withTemperature(0.0d)
                    .withTopP(1.0d)
                    .withSeed(42)
                    .build();

            String population = chatClient
                    .prompt()
                    .user(promptText)
                    .options(options)
                    .call()
                    .content();
            return population;
        }
        return "Country Name is blank";
    }

    public String getAIMessage(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
    @Async
    public CompletableFuture<String> getAIPopulation(String countryName) {
        System.out.println("Execute method getAIPopulation asynchronously countryName="+countryName+",thread="+Thread.currentThread().getName());
        return CompletableFuture.completedFuture(getPopulation(countryName));
    }

    /*
    public ResponseEntity<ApiResponse> callExternalServiceGetEmployees() {
        String employeeUrl = "http://localhost:3030/spring-test-proj/api/failover/getAllEmployees";
        return restTemplate.exchange(employeeUrl, HttpMethod.GET, null, ApiResponse.class);
    }
    */
}
