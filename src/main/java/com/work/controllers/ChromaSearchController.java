package com.work.controllers;

import com.google.gson.Gson;
import com.work.service.ChromaLoadDocumentService;
import com.work.service.MyChromaServices;
import com.work.service.OllamaChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chroma-search")
public class ChromaSearchController {

    @Autowired
    private MyChromaServices myChromaServices;

    @Autowired
    private OllamaChatService ollamaChatService;

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

    @PostMapping("/search-documents")
    public ResponseEntity<String> searchDocuments( @RequestParam("searchText") String searchText) {
        String content = myChromaServices.searchDocuments(searchText);
        if (content != null) {
            return new ResponseEntity(content, HttpStatus.OK);
        }
        return new ResponseEntity("Content Not Found", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/search-text")
    public ResponseEntity<String> searchText( @RequestParam("searchText") String searchText) {
        String content = ollamaChatService.manualRAG(searchText);
        if (content != null) {
            return new ResponseEntity(content, HttpStatus.OK);
        }
        return new ResponseEntity("Content Not Found", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
