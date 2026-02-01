package com.work.service;

import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChromaLoadDocumentService {

    private final VectorStore vectorStore;

    // Spring Boot auto-configures the VectorStore bean if the starter is present
    public ChromaLoadDocumentService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public boolean loadFile(MultipartFile file) {
        try {
            String folderPath = "/Users/kishorkkona/kona_new/kishor-all-projects/spring-ai-advance-project/src/main/resources/";
            Path filePath = Path.of(folderPath+file.getOriginalFilename());
            File f = filePath.toFile();
            if(f.exists()) {
                System.out.println("File exists at: " + f.getAbsolutePath());
                return ingestWordDocument(filePath);
            } else {
                System.out.println("File does not exist at: " + filePath);
            }
            //ingestWordDocument(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean ingestWordDocument(Path filePath) {
        try {
            // 1. Load & Parse using LangChain4j (Apache Tika)
            var langchainDoc = FileSystemDocumentLoader.loadDocument(
                    filePath,
                    new ApacheTikaDocumentParser()
            );

            // 2. Split into chunks.
            // 1000 tokens per chunk with 100 token overlap ensures context isn't lost at edges.
            var splitter = new DocumentByParagraphSplitter(1000, 100);
            var segments = splitter.split(langchainDoc);

            // 3. Map LangChain4j segments to Spring AI Documents
            List<Document> springAiDocs = segments.stream()
                    .map(segment -> new Document(
                            segment.text(),
                            //segment.metadata().asMap() // Preserves metadata like fileName
                            segment.metadata().toMap()
                    ))
                    .collect(Collectors.toList());

            // 4. Batch Upload to ChromaDB
            // Spring AI handles the embedding generation via your configured EmbeddingModel
            vectorStore.add(springAiDocs);

            System.out.println("Ingested " + springAiDocs.size() + " chunks into ChromaDB.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}