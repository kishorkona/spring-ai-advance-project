package com.work.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Value("file:src/main/resources/ai_3_vector_database_and_RAG.docx") // Path to your word document
    private Resource wordDocumentResource;

    // ... processor and writer beans (omitted for brevity) ...

    @Bean
    public DocxItemReader customDocxReader() {
        DocxItemReader reader = new DocxItemReader();
        reader.setResource(wordDocumentResource);
        return reader;
    }
    @Bean
    public Job job(JobRepository jobRepository, Step processWordDocumentStep) {
        return new JobBuilder("wordDocumentJob", jobRepository)
                .start(processWordDocumentStep)
                .build();
    }

    @Bean
    public Step processWordDocumentStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                        ItemProcessor<String, String> processor, ItemWriter<String> writer) {
        return new StepBuilder("processWordDocumentStep", jobRepository)
                .<String, String>chunk(10, transactionManager) // Chunk size of 10 items
                .reader(customDocxReader())
                .processor(processor)
                .writer(writer)
                .build();
    }
}
