package com.project.chatai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/huggingface/chat")
public class HuggingFaceChatController {

  private final ChatClient chatClient;

  private static final String SYSTEM_PROMPT =
      "You are a senior engineer. Generate code based on the given description. " +
          "Ensure the code is idiomatic, efficient, and follows best practices.";

  public HuggingFaceChatController(@Qualifier("huggingFaceChatClient") ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @PostMapping("/generate-code")
  public ChatResponse generateCode(@RequestBody String message) {
    return this.chatClient.prompt()
        .system(SYSTEM_PROMPT)
        .user(message)
        .call()
        .chatResponse();
  }
}