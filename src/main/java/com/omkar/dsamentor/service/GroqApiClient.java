package com.omkar.dsamentor.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class GroqApiClient {

        private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

        private static final String MODEL = "openai/gpt-oss-20b";

        private static final HttpClient CLIENT = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(20))
                        .build();

        private static final ObjectMapper MAPPER = new ObjectMapper();

        public static String generateText(String systemPrompt,String userPrompt,double temperature)throws IOException, InterruptedException {

                // 1. Read the API key
                String apiKey = System.getenv("GROQ_API_KEY");

                if (apiKey == null || apiKey.isBlank())
                {
                        throw new IllegalStateException("GROQ_API_KEY environment variable is missing.");
                }

                // 2. Create the JSON request body
                ObjectNode requestBody = MAPPER.createObjectNode();

                requestBody.put("model", MODEL);
                requestBody.put("temperature", temperature);
                requestBody.put("max_completion_tokens", 2600);

                ArrayNode messages = requestBody.putArray("messages");

                ObjectNode systemMessage = messages.addObject();
                systemMessage.put("role", "system");
                systemMessage.put("content", systemPrompt);

                ObjectNode userMessage = messages.addObject();
                userMessage.put("role", "user");
                userMessage.put("content", userPrompt);

                String jsonBody = MAPPER.writeValueAsString(requestBody);

                // 3. Build the HTTP request
                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(API_URL))
                                .timeout(Duration.ofSeconds(60))
                                .header("Authorization", "Bearer " + apiKey)
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                                .build();

                // 4. Send the request to Groq
                HttpResponse<String> response = CLIENT.send(request,HttpResponse.BodyHandlers.ofString());

                // 5. Check whether the API request succeeded
                if (response.statusCode() < 200 || response.statusCode() >= 300)
                {
                        throw new IOException(
                                        "Groq API returned HTTP "
                                                        + response.statusCode()
                                                        + ": " + response.body());
                }

                // 6. Parse the JSON response
                JsonNode root = MAPPER.readTree(response.body());

                JsonNode content = root.path("choices")
                                .path(0)
                                .path("message")
                                .path("content");

                if (!content.isTextual()) {
                        throw new IOException(
                                        "Groq returned no text response.");
                }

                return content.asText();
        }
}