package code.server;

import code.client.Model.AppConfig;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;

import org.json.JSONObject;
import java.util.Base64;

public class DallERequestHandler extends RecipeToImage implements HttpHandler {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/images/generations";
    private static final String MODEL = "dall-e-2";
    private static final int NUM_IMAGES = 1;
    private static final String IMAGE_SIZE = "256x256";
    private static final String RESPONSE_FORMAT = "b64_json";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request received";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        try {
            String recipeTitle = query.substring(query.indexOf("=") + 1);
            recipeTitle = URLEncoder.encode(recipeTitle, "UTF-8");
            response = getResponse(recipeTitle);
        } catch (InterruptedException e) {
            response = "Error";
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    private String getResponse(String prompt) throws IOException, InterruptedException {
        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("n", NUM_IMAGES);
        requestBody.put("size", IMAGE_SIZE);
        requestBody.put("response_format", RESPONSE_FORMAT);

        // Create the HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Create the request object
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(API_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", String.format("Bearer %s", AppConfig.API_KEY))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        // Send the request and receive the response
        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        // Process the response
        String responseBody = response.body();
        return processResponse(responseBody);
    }

    private String processResponse(String responseBody) {
        JSONObject responseJson = new JSONObject(responseBody);
        String generatedImageData = "";
        try {
            generatedImageData = responseJson.getJSONArray("data")
                    .getJSONObject(0).getString("b64_json");
        } catch (Exception chatError) {
            // badly formatted json
            File file = new File(AppConfig.RECIPE_IMG_FILE);
            try {
                byte[] imageBytes = Files.readAllBytes(file.toPath());
                generatedImageData = Base64.getEncoder().encodeToString(imageBytes);
            } catch (Exception fileError) {
                fileError.printStackTrace();
            }
            chatError.printStackTrace();
        }
        return generatedImageData;
    }

}