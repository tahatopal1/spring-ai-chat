package com.project.chatai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.huggingface.HuggingfaceChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIProviderConfig {

  @Bean("openAIChatClient")
  ChatClient openAIChatClient(OpenAiChatModel openAiChatModel) {
    return ChatClient.builder(openAiChatModel)
        .defaultOptions(OpenAiChatOptions.builder()
            .temperature(1.0)
            .build())
        .build();
  }

  @Bean("vertexAIChatClient")
  ChatClient vertexAIChatClient(VertexAiGeminiChatModel vertexAiGeminiChatModel) {
    return ChatClient.builder(vertexAiGeminiChatModel).build();
  }

  @Bean("huggingFaceChatClient")
  ChatClient huggingFaceChatClient(HuggingfaceChatModel huggingfaceChatModel) {
    return ChatClient.builder(huggingfaceChatModel).build();
  }

}
