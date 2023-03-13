package com.project.stressAI.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.stressAI.model.OpenAI;
import com.project.stressAI.model.OpenAiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiService
{

 @Autowired
 private ObjectMapper objectMapper;

 private static final String OPENAI_URL = "https://api.openai.com/v1/completions";

 private final String apiKey = "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";
 private final RestTemplate restTemplate = new RestTemplate();

// public String generateImages(String prompt, float temperature, int maxTokens, String stop, final int logprobs, final boolean echo)

 public String textResponse(String prompt) throws JsonProcessingException {
  HttpHeaders headers = new HttpHeaders();
  headers.setContentType(MediaType.APPLICATION_JSON);
  headers.set("Authorization", "Bearer " + apiKey);
  OpenAI openAI = new OpenAI();
  openAI.setModel("text-davinci-003");
  openAI.setPrompt(prompt);
  openAI.setTemperature(Double.valueOf(0));
  openAI.setMax_tokens(Integer.valueOf(100));
  openAI.setTop_p(Integer.valueOf(1));
  openAI.setFrequency_penalty(Integer.valueOf(0));
  openAI.setPresence_penalty(Double.valueOf(0));

  String req = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString( openAI);

  HttpEntity<String> request = new HttpEntity<>(req, headers);
  ResponseEntity<OpenAiResponse> response = restTemplate.postForEntity(OPENAI_URL, request, OpenAiResponse.class);

  return response.getBody().getChoices().get(0).getText();
 }
}