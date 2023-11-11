package code.client.Model;

import java.io.IOException;
import java.net.URISyntaxException;

public interface ITextToRecipe {
    String buildPrompt(String typeOfMeal, String input);

    String getChatGPTResponse(String typeOfMeal, String input) throws IOException, InterruptedException, URISyntaxException;

    Recipe mapResponseToRecipe(String responseText);
}
