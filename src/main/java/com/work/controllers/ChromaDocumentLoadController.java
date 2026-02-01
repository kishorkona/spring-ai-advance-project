package com.work.controllers;

import com.google.gson.Gson;
import com.work.data.PostDocuments;
import com.work.service.ChromaLoadDocumentService;
import com.work.service.MyChromaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chroma-load")
public class ChromaDocumentLoadController {

    @Autowired
    private MyChromaServices myChromaServices;

    @Autowired
    private ChromaLoadDocumentService chromaLoadDocumentService;

    Gson gson = new Gson();

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

    @PostMapping(path="/loadDocument", consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<String> loadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("details") String jsonData) {
        System.out.println("Received documentName: " + file);
        if(chromaLoadDocumentService.loadFile(file)) {
            return new ResponseEntity<>("File loaded to chromadb" + file.getOriginalFilename(), HttpStatus.OK);
        }
        return new ResponseEntity<>("File load Failed" + file.getName(), HttpStatus.OK);
    }
}
