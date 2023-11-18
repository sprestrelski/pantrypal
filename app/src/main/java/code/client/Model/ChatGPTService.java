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

public class ChatGPTService implements ITextToRecipe {
    public static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    public static final String API_KEY = "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";
    public static final String MODEL = "text-davinci-003";
    public static final int MAX_TOKENS = 500;
    public static final double TEMPERATURE = 1.;

    @Override
    public String getChatGPTResponse(String mealType, String ingredients)
            throws IOException, InterruptedException, URISyntaxException {
        // Set request parameters
        String prompt = buildPrompt(mealType, ingredients);

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
    public Recipe mapResponseToRecipe(String mealType, String responseText) {
        char firstChar = mealType.charAt(0);
        String lowerCased = mealType.toLowerCase();
        String mealTypeTitle = Character.toUpperCase(firstChar) + lowerCased.substring(1);

        // Split the tokens into lines
        String[] tokenArr = responseText.split("\n");
        List<String> tokenList = new ArrayList<>(Arrays.asList(tokenArr));
        int i;

        // Remove empty tokens
        for (i = 0; i < tokenList.size();) {
            if (tokenList.get(i).isBlank()) {
                tokenList.remove(i);
            } else {
                ++i;
            }
        }

        // Create a new recipe with a title
        Recipe recipe = new Recipe(mealTypeTitle + ": " + tokenList.get(0));

        // Parse recipe's ingredients
        String ingredient;
        for (i = 2; !tokenList.get(i).equals("Instructions:"); ++i) {
            ingredient = removeDashFromIngredient(tokenList.get(i).trim());
            recipe.addIngredient(ingredient);
        }

        // Parse recipe's instructions
        String instruction;
        for (i += 1; i < tokenList.size(); ++i) {
            instruction = removeNumberFromInstruction(tokenList.get(i).trim());
            recipe.addInstruction(instruction);
        }

        return recipe;
    }

    private String removeDashFromIngredient(String ingredient) {
        return ingredient.substring(2);
    }

    private String removeNumberFromInstruction(String instruction) {
        StringBuilder strBuilder = new StringBuilder();
        int i;

        // Ignore characters until '.'
        for (i = 0; i < instruction.length() && (instruction.charAt(i) != '.'); ++i)
            ;

        // Ignore '.' and ' ' after
        // Get all characters until end of string
        for (i += 2; i < instruction.length(); ++i) {
            strBuilder.append(instruction.charAt(i));
        }

        return strBuilder.toString();
    }

    public String buildPrompt(String mealType, String ingredients) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("I am a student on a budget with a busy schedule and I need to quickly cook a ")
                .append(mealType)
                .append(" ")
                .append(ingredients)
                .append(" Make a recipe using only these ingredients plus condiments. ")
                .append("Remember to first include a title, then a list of ingredients, and then a list of instructions.");
        return prompt.toString();
    }

}