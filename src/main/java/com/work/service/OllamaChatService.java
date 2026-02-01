package com.work.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OllamaChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public OllamaChatService(ChatClient.Builder builder, VectorStore vectorStore) {
        // The QuestionAnswerAdvisor automates:
        // 1. Searching ChromaDB 2. Injecting results into the prompt
        this.vectorStore = vectorStore;
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(this.vectorStore))
                .build();
    }
    public String getProperSentence(String userPrompt) {
        return chatClient.prompt()
                .user(userPrompt)
                .call()
                .content();
    }

    public String manualRAG(String query) {
        // 1. Get docs from ChromaDB
        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.query(query).withTopK(2)
        );

        // 2. Join documents into a context string
        String context = docs.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n"));

        // 3. Send to Phi-3 manually
        return chatClient.prompt()
                .user(u -> u.text("Use this context: {context}. Question: {query}")
                        .param("context", context)
                        .param("query", query))
                .call()
                .content();
    }
}
