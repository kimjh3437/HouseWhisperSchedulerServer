package com.housewhisper.housewhisperschedulerserver.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigService {
    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${spring.ai.openai.chat.options.model:gpt-4o}")  // Defaulting to a specific model
    private String modelName;

    @Bean
    public ChatModel chatModel() {
        var openAiApi = new OpenAiApi(apiKey);
        var openAiChatOptions = OpenAiChatOptions.builder()
                .model(modelName)
                .temperature(0.7)
                .maxTokens(5000)
                .build();
        return new OpenAiChatModel(openAiApi, openAiChatOptions);
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.create(chatModel);
    }
}
