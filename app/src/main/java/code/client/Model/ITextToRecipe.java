package code.client.Model;

import java.io.IOException;
import java.net.URISyntaxException;

public interface ITextToRecipe {
    String buildPrompt(String input);

    String getChatGPTResponse(String input) throws IOException, InterruptedException, URISyntaxException;

    Recipe mapResponseToRecipe(String responseText);
}
