package com.work.controllers;

import com.google.gson.Gson;
import com.work.data.PostDocuments;
import com.work.service.MyChromaServices;
import com.work.service.MyOllamaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chroma")
public class ChromaTestController {

    @Autowired
    private MyChromaServices myChromaServices;

    Gson gson = new Gson();

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        String response = myChromaServices.ping();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/version")
    public ResponseEntity<String> version() {
        String response = myChromaServices.version();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/add-documents")
    public ResponseEntity<String> addDocuments(@RequestBody List<PostDocuments> documents) {
        if(documents != null && documents.size() >0) {
            List<PostDocuments> finalDocs = documents.stream().filter(x -> {
                if(x.getMetadata() != null || x.getMetadata().size()>0) {
                    return true;
                }
                return false;
            }).collect(Collectors.toUnmodifiableList());

            if(finalDocs != null && finalDocs.size() >0) {
                boolean response = myChromaServices.addDocuments(documents);
                if (response) {
                    return new ResponseEntity("Documents added successfully", HttpStatus.OK);
                }
                return new ResponseEntity("Failed to add", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity("Metadata is empty for some documents", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("Documents are Empty", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/search-documents")
    public ResponseEntity<String> searchDocuments( @RequestParam("searchText") String searchText) {
        String content = myChromaServices.searchDocuments(searchText);
        if (content != null) {
            return new ResponseEntity(content, HttpStatus.OK);
        }
        return new ResponseEntity("Content Not Found", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
