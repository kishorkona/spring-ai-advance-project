package com.work.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AiConfig {
    @Bean
    @Primary
    public ChatModel defaultChatModel(OllamaChatModel ollamaChatModel) {
        return ollamaChatModel;
    }
}
