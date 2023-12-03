package code.client.Model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;
import org.bson.types.ObjectId;
import java.util.Base64;

public class DallEService extends RecipeToImage {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/images/generations";
    private static final String MODEL = "dall-e-2";
    private static final int NUM_IMAGES = 1;
    private static final String IMAGE_SIZE = "256x256";
    private static final String RESPONSE_FORMAT = "b64_json";

    @Override
    public String getResponse(String prompt) throws IOException, InterruptedException {
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
        JSONObject responseJson = new JSONObject(responseBody);
        String generatedImageData = "";
        try {
            generatedImageData = responseJson.getJSONArray("data")
                    .getJSONObject(0).getString("b64_json");
        } catch (Exception e) {
            // badly formatted json
            generatedImageData = "An error occurred.";
            e.printStackTrace();
        }
        return generatedImageData;
    }

    @Override
    public byte[] downloadImage(String generatedImageData, ObjectId id) {
        // convert base64 string to binary data
        byte[] generatedImageBytes = Base64.getDecoder().decode(generatedImageData);
        try (InputStream in = new ByteArrayInputStream(generatedImageBytes)) {
            String path = id.toString() + ".jpg";
            Files.copy(in, Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return generatedImageBytes;
    }

}
