package code.client.Model;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class TextToRecipe implements ITextToRecipe {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";
    private static final int MAX_TOKENS = 500;
    private static final double TEMPERATURE = 1.;

    @Override
    public String getChatGPTResponse(String typeOfMeal, String input) throws IOException, InterruptedException, URISyntaxException {
        // Set request parameters
        String prompt = buildPrompt(typeOfMeal, input);

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
                .header("Authorization", String.format("Bearer %s", API_KEY))
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
    public Recipe mapResponseToRecipe(String responseText) {
        // System.out.println(responseText);
        String[] tokenArr = responseText.split("\n");
        List<String> tokenList = new ArrayList<>(Arrays.asList(tokenArr));
        int i = 0;
        // Remove empty tokens
        for (; i < tokenList.size();) {
            if (tokenList.get(i).isBlank()) {
                tokenList.remove(i);
            } else {
                ++i;
            }
        }

        // Create a new recipe with a title
        Recipe recipe = new Recipe("1", tokenList.get(0));

        // Parse recipe's ingredients
        String ingredient;
        i = 2;
        for (; !tokenList.get(i).equals("Instructions:"); ++i) {
            // Remove leading "-"
            ingredient = tokenList.get(i).trim();
            recipe.addIngredient(ingredient);
        }

        // Parse recipe's instructions
        String instruction;
        ++i;
        for (; i < tokenList.size(); ++i) {
            // Remove leading numbers
            instruction = tokenList.get(i).trim();
            recipe.addInstruction(instruction);
        }

        return recipe;
    }

    public String buildPrompt(String typeOfMeal, String input) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("I am a student on a budget with a busy schedule and I need to quickly cook a ")
                .append(typeOfMeal + " ")
                .append(input)
                .append(" Make a recipe using only these ingredients plus condiments. ")
                .append("Remember to first include a title, then a list of ingredients, and then a list of instructions.");
        return prompt.toString();
    }

}