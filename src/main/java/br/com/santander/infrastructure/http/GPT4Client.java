package br.com.santander.infrastructure.http;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

@ApplicationScoped
public class GPT4Client {

    private static final String GPT_API_URL = "https://api.openai.com/v1/completions";

    @ConfigProperty(name = "services.gpt.api-key")
    public String apiKey;

    private final Client client;

    public GPT4Client() {
        this.client = ClientBuilder.newClient();
    }

    public Optional<String> generateText(String prompt) {
        try {
            Response response = client.target(GPT_API_URL)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + apiKey)
                    .post(Entity.json(buildRequestBody(prompt)));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String jsonResponse = response.readEntity(String.class);
                return extractGeneratedText(jsonResponse);
            } else {
                System.err.println("Failed to generate text: " + response.getStatus());
            }
        } catch (Exception e) {
            System.err.println("Error while generating text: " + e.getMessage());
        }
        return Optional.empty();
    }

    private String buildRequestBody(String prompt) {
        return """
                {
                    "model": "gpt-4o-mini",
                    "prompt": "%s",
                    "max_tokens": 100,
                    "temperature": 0.7
                }
                """.formatted(prompt);
    }

    private Optional<String> extractGeneratedText(String jsonResponse) {
        try {
            JsonObject jsonObject = new JsonParser().parse(jsonResponse).getAsJsonObject();
            return Optional.of(jsonObject.getAsJsonArray("choices")
                    .get(0)
                    .getAsJsonObject()
                    .get("text")
                    .getAsString()
                    .trim());
        } catch (Exception e) {
            System.err.println("Error parsing GPT-4 response: " + e.getMessage());
        }
        return Optional.empty();
    }
}
