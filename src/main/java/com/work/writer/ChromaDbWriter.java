package com.work.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class ChromaDbWriter implements ItemWriter<String> {

    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;

    public ChromaDbWriter() {
        // Initialize ChromaDB client (Assumes running on localhost:8000) [12]
        this.embeddingStore = ChromaEmbeddingStore.builder()
                .baseUrl("http://localhost:8000")
                .collectionName("documents")
                .build();
        this.embeddingModel = new AllMiniLmL6V2EmbeddingModel();
    }

    @Override
    public void write(Chunk<? extends String> chunk) {
        for (String text : chunk) {
            TextSegment segment = TextSegment.from(text, new Metadata());
            Embedding embedding = embeddingModel.embed(segment).content();
            embeddingStore.add(embedding, segment); // Add to ChromaDB [1]
        }
    }
}