package com.work.controllers;

import com.google.gson.Gson;
import com.work.data.PostDocuments;
import com.work.service.MyOllamaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
public class AIOllamaTestController {

    @Autowired
    private MyOllamaServices myOllamaServices;

    @GetMapping("/getTextMessage")
    public ResponseEntity<String> getTextMessage() {
        List<PostDocuments> messages = new ArrayList<>();
        PostDocuments document = new PostDocuments();
        document.setUniqueId("ond01");
        document.setContent("This is first document content");
        Map<String, String> metadata = new HashMap<>();
        metadata.put("source", "manual.pdf");
        metadata.put("meta2", "meta2");
        document.setMetadata(metadata);
        messages.add(document);
        Gson gson = new Gson();
        System.out.println("Messages:::" + gson.toJson(messages));

        return new ResponseEntity("Hi Kishor", HttpStatus.OK);
    }

    //http://localhost:8080/ai/chat?message=What is Spring AI?
    @GetMapping("/ai/chat")
    public String generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        System.out.println("Kishor::: message: " + message);
        return myOllamaServices.getAIMessage(message);
    }

    @GetMapping("/getPopulation/{countryName}")
    public String getPopulation(@PathVariable("countryName") String countryName) {
        return myOllamaServices.getPopulation(countryName);
    }

    @GetMapping("/getAsyncMessage")
    public ResponseEntity<List<String>> getAsyncMessage() {
        try {
            System.out.println("asynchronously rest method getAsyncMessage=" + Thread.currentThread().getName());
            List<CompletableFuture<String>> futuresList = List.of(
                    myOllamaServices.getWelcomeMessage(),
                    myOllamaServices.getWelcomeMessage(),
                    myOllamaServices.getWelcomeMessage(),
                    myOllamaServices.getWelcomeMessage(),
                    myOllamaServices.getWelcomeMessage()
            );
            CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[0])).join();
            List<String> results = futuresList.stream().map(CompletableFuture::join).toList();
            return new ResponseEntity(results, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity("Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("/getPopulationForCountries")
    public ResponseEntity<List<String>> getPopulationForCountries(
            @RequestParam("countryNames") List<String> countryNames) {
        try {
            System.out.println("asynchronously rest method getAsyncMessage=" + Thread.currentThread().getName());
            List<CompletableFuture<String>> futuresList = countryNames.stream().map(x ->{
                return myOllamaServices.getAIPopulation(x);
            }).collect(Collectors.toUnmodifiableList());
            CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[0])).join();
            List<String> results = futuresList.stream().map(CompletableFuture::join).toList();
            return new ResponseEntity(results, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity("Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
