package code.server;

import code.client.Model.AppConfig;
import code.client.Controllers.Format;
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGPTRequestHandler extends TextToRecipe implements HttpHandler {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String MODEL = "text-davinci-003";
    private static final int MAX_TOKENS = 500;
    private static final double TEMPERATURE = 1.;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request received";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        try {
            String value = query.substring(query.indexOf("=") + 1);
            String[] typeIngredients = value.split("::");
            String mealType = typeIngredients[0];
            String ingredients = typeIngredients[1];
            response = getResponse(mealType, ingredients);
        } catch (IndexOutOfBoundsException e) {
            response = "Provide valid meal type or ingredients";
            e.printStackTrace();
        } catch (InterruptedException | URISyntaxException | ConnectException e) {
            response = "An error occurred.";
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    private String getResponse(String mealType, String ingredients)
            throws IOException, InterruptedException, URISyntaxException {
        // Set request parameters
        Format format = new Format();
        String prompt = format.buildPrompt(mealType, ingredients);

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

    @Override
    public void setSampleRecipe(String recipe) {
    }
}