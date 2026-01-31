package com.work.config;

import org.springframework.ai.autoconfigure.vectorstore.chroma.ChromaConnectionDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Configuration
public class CromaConfig {
    /*
    @Bean
    public ChromaConnectionDetails chromaConnectionDetails() {
        return new ChromaConnectionDetails() {
            @Override
            public String getHost() {
                // Change to your local Chroma host (usually http://localhost)
                return "http://localhost";
            }

            @Override
            public int getPort() {
                return 3030;
            }

            @Override
            public String getKeyToken() {
                return null; // Return null if no API key is used locally
            }
        };
    }
    */
    @Bean
    @Primary
    public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        return factory;
    }
}
