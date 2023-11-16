package code.client.Model;

import java.io.IOException;
import java.net.URISyntaxException;

public interface ITextToRecipe {
    String buildPrompt(String mealType, String ingredients);

    String getChatGPTResponse(String mealType, String ingredients)
            throws IOException, InterruptedException, URISyntaxException;

    Recipe mapResponseToRecipe(String mealType, String responseText);
}
