package code.server;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import code.AppConfig;

public class ChatGPTService implements TextToRecipe {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String MODEL = "text-davinci-003";
    private static final int MAX_TOKENS = 500;
    private static final double TEMPERATURE = 1.;

    @Override
    public String getResponse(String mealType, String ingredients)
            throws IOException, InterruptedException, URISyntaxException {
        // Set request parameters
        PromptBuilder promptBuilder = new PromptBuilder(mealType, ingredients);
        String prompt = promptBuilder.getPrompt();

        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", MAX_TOKENS);
        requestBody.put("temperature", TEMPERATURE);

        // Create the HTTP Client
        HttpClient client = HttpClient.newHttpClient();

        // Create the request object
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(API_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", String.format("Bearer %s", AppConfig.API_KEY))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        // Process the response
        String responseBody = response.body();
        JSONObject responseJson = new JSONObject(responseBody);
        JSONArray choices = responseJson.getJSONArray("choices");
        String responseText = choices.getJSONObject(0).getString("text");
        return responseText;
    }
}
