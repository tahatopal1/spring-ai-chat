package com.project.chatai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/openai/chat")
public class OpenAIChatController {

  private final ChatClient chatClient;

  private final String systemPrompt = "You are a helpful assistant to summarize any given content."
      + " Make sure the summary is concise, informative, etc."
      + " Do not answer anything other than the summarization."
      + " If the question is not about summarization,"
      + " response with 'I can only help with summarization tasks'.";

  public OpenAIChatController(@Qualifier("openAIChatClient") ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @PostMapping("/summarize")
  public ChatClientResponse summarize(@RequestBody String message) {
    return chatClient.prompt()
        .system(systemPrompt) // Setting the guardrail
        .user(message)
        .call()
        .chatClientResponse();
  }

}
