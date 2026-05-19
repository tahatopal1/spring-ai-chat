package com.project.chatai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatai.exception.OpenAIChatException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAIService {

  private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
  private static final String CONTENT_TYPE = "application/json";
  private final String SYSTEM_PROMPT = "You are a helpful assistant to summarize any given content."
      + " Make sure the summary is concise, informative, etc."
      + " Do not answer anything other than the summarization."
      + " If the question is not about summarization,"
      + " response with 'I can only help with summarization tasks'.";


  @Value("${spring.ai.openai.api-key}")
  private String apiKey;

  @Value("${spring.ai.openai.chat.options.model}")
  private String model;

  private final ObjectMapper objectMapper;

  public String chat(String prompt) throws OpenAIChatException {
    try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
      var request = getRequest(prompt);
      var response = httpClient.execute(request, resp -> EntityUtils.toString(resp.getEntity()));
      return parseResponse(response);
    } catch (IOException e) {
      throw new OpenAIChatException("Could not connect to the OpenAI API", e);
    }
  }

  private String parseResponse(String response) throws JsonProcessingException {
    Map<String, Object> openAIResponse = objectMapper.readValue(response, Map.class);
    return openAIResponse.get("choices").toString();
  }

  private HttpPost getRequest(String prompt) throws JsonProcessingException {
    var request = new HttpPost(OPENAI_API_URL);
    request.addHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);

    Map<String, String> userMessage = Map.of(
        "role", "user",
        "content", prompt
    );

    Map<String, String> systemMessage = Map.of(
        "role", "system",
        "content", SYSTEM_PROMPT
    );

    Map<String, Object> body = Map.of(
        "model", model,
        "messages", List.of(userMessage, systemMessage)
    );

    String requestBody = objectMapper.writeValueAsString(body);
    request.setEntity(new StringEntity(requestBody));
    return request;

  }


}
