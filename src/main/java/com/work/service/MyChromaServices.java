package com.work.service;

import com.google.gson.Gson;
import com.work.data.PostDocuments;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MyChromaServices {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    VectorStore vectorStore;

    Gson gson = new Gson();

    String mainUrl = "http://localhost:3030/api/";

    public String ping() throws ResourceAccessException {
        //record PingResponse(String heartbeat) {};
        System.out.println("------------>> test");
        String pingUrl = mainUrl+"v1/heartbeat";

        //ResponseEntity<PingResponse> rslt = restTemplate.exchange(pingUrl, HttpMethod.GET, null, PingResponse.class);
        //return rslt.getBody().getHeartbeat();
        return null;
    }

    public String version() throws ResourceAccessException {
        //record PingResponse(String heartbeat) {};
        System.out.println("------------>> test");
        String pingUrl = mainUrl+"v1/version";

        ResponseEntity<String> rslt = restTemplate.exchange(pingUrl, HttpMethod.GET, null, String.class);
        return rslt.getBody();
    }


    public boolean addDocuments(List<PostDocuments> documents) throws ResourceAccessException {
        try {
            List<Document> vectorDocs = documents.stream()
                    .map(doc -> {
                        Map<String, Object> metaMap = doc.getMetadata().entrySet()
                                .stream()
                                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

                        if(doc.getUniqueId() != null && !doc.getUniqueId().isEmpty()) {
                            return new Document(doc.getUniqueId(), doc.getContent(), metaMap);
                        }
                        return new Document(doc.getContent(), metaMap);
                    })
                    .toList();
            vectorStore.add(vectorDocs);
            /*
            List<Document> documents = List.of(
                    new Document("unique-id-123", "Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
                    new Document("unique-id-124", "The World is Big and Salvation Lurks Around the Corner", Map.of("meta2", "meta2")),
                    new Document("unique-id-125", "You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));
            vectorStore.add(documents);
            */
            return  true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String searchDocuments(String searchText) throws ResourceAccessException {
        try {
            // 1. Configure the SearchRequest
            // topK(5) retrieves the 5 most similar chunks
            // similarityThreshold(0.7) ensures results are reasonably relevant
            SearchRequest request = SearchRequest.query(searchText)
                    .withTopK(5)
                    .withSimilarityThreshold(0.7);

            // 2. Perform similarity search in ChromaDB
            List<Document> results = vectorStore.similaritySearch(request);

            // 3. Join the text of all matching documents to get "all details"
            String data = results.stream()
                    .map(Document::getContent)
                    .collect(Collectors.joining("\n--- Next Segment ---\n"));

            System.out.println("Found Document Content: " + data);
            return  data;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String advanceSearch(String searchText) throws ResourceAccessException {
        try {
            // 1. Configure the SearchRequest
            // topK(5) retrieves the 5 most similar chunks
            // similarityThreshold(0.7) ensures results are reasonably relevant
            SearchRequest request = SearchRequest.query(searchText)
                    .withTopK(5)
                    //.withSimilarityThreshold(0.7);
                    .withSimilarityThresholdAll();

            // 2. Perform similarity search in ChromaDB
            List<Document> results = vectorStore.similaritySearch(request);

            // 3. Join the text of all matching documents to get "all details"
            String data = results.stream()
                    .map(Document::getContent)
                    .collect(Collectors.joining("\n--- Next Segment ---\n"));

            System.out.println("Found Document Content: " + data);
            return  data;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
