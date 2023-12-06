package code.server;

import java.io.IOException;
import java.net.URISyntaxException;

public interface TextToRecipe {
    String getResponse(String mealType, String ingredients)
            throws IOException, InterruptedException, URISyntaxException;
}
