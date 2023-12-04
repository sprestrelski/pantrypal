package code.server;

import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.*;

public class MockChatGPTRequestHandler extends TextToRecipe implements HttpHandler {

    private String sampleRecipe = """
            Fried Chicken
            breakfast
            Ingredients:
            - 2 chicken breasts, diced
            - 2 eggs
            Instructions:
            1. Crack 2 eggs into bowl.
            2. Add chicken into bowl and then fry.
            3. Enjoy!
            """;

    @Override
    public void setSampleRecipe(String recipeText) {
        sampleRecipe = recipeText;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, sampleRecipe.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(sampleRecipe.getBytes());
        outStream.close();

    }
}
