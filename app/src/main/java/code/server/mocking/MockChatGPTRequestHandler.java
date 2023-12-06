package code.server.mocking;

import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.*;

public class MockChatGPTRequestHandler implements HttpHandler {
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
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        if (method.equals("GET2")) {
            sampleRecipe = """
                    Fried Chicken and Egg Fried Rice
                    breakfast
                    Ingredients:

                    - 2 chicken breasts, diced
                    - 2 large eggs
                    - 2 cups cooked rice
                    - 2 tablespoons vegetable oil
                    - 2 tablespoons soy sauce
                    - 1 teaspoon sesame oil
                    - Salt and pepper to taste""";
        }
        
        httpExchange.sendResponseHeaders(200, sampleRecipe.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(sampleRecipe.getBytes());
        outStream.close();
    }
}
