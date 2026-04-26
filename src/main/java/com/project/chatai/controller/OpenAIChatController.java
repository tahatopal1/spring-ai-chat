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

  private final String SYSTEM_PROMPT = "You are a helpful assistant to summarize any given content."
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
        .system(SYSTEM_PROMPT) // Setting the guardrail
        .user(message)
        .call()
        .chatClientResponse();
  }

  @PostMapping("/template")
  public String summarizeSpecifically(@RequestBody String introduction) {
    return chatClient.prompt()
        .system(SYSTEM_PROMPT)
        .user(u -> u.text("I want to summarize the following business introduction: \"{introduction}\""
            + "Use following example to construct your response:\n"
            + "Company introduction:\n"
            + "\"Turkey’s largest technology consultancy, pioneering product development and technology consulting"
            + " for an AI-native future. With more than 20 years of experience,"
            + " we empower leading companies in banking and finance, insurance ecosystem.\"\n"
            + "Example Output:\n"
            + "\" Features:\n"
            + "* Large technology company\n"
            + "* +20 years of experiance\n"
            + "  Domains:\n"
            + "* Banking\n"
            + "* Finance\n"
            + "* Insurance\" ")
            .param("introduction", introduction))
        .call()
        .content();
  }

}
