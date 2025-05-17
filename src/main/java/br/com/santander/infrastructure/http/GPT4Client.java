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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@ApplicationScoped
public class GPT4Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(GPT4Client.class);

    @ConfigProperty(name = "services.gpt.api.url")
    public String gtpApiUrl;
    @ConfigProperty(name = "services.gpt.api-key")
    public String apiKey;

    private final Client client;

    public GPT4Client() {
        this.client = ClientBuilder.newClient();
    }

    public Optional<String> generateText(String prompt) {
        LOGGER.info("Generating text with prompt: {}", prompt);

        Response response = client.target(gtpApiUrl)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiKey)
                .post(Entity.json(buildRequestBody(prompt)));

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            LOGGER.error("Error response from GPT-4 API: {}", response.getStatus());
            return Optional.empty();
        }

        String jsonResponse = response.readEntity(String.class);
        return extractGeneratedText(jsonResponse);
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
        LOGGER.info("Parsing GPT-4 response: {}", jsonResponse);
        try {
            JsonObject jsonObject = new JsonParser().parse(jsonResponse).getAsJsonObject();
            return Optional.of(jsonObject.getAsJsonArray("choices")
                    .get(0)
                    .getAsJsonObject()
                    .get("text")
                    .getAsString()
                    .trim());
        } catch (Exception e) {
            LOGGER.error("Error parsing GPT-4 response: {}", e.getMessage());
        }
        return Optional.empty();
    }
}
