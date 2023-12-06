package code.server.mocking;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import com.sun.net.httpserver.*;

import code.server.TextToRecipe;

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
        String method = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String response = "Error";
        try {
            String value = query.substring(query.indexOf("=") + 1);
            String[] typeIngredients = value.split("::");
            String mealType = typeIngredients[0];
            String ingredients = typeIngredients[1];

            if (mealType.toLowerCase().equals("breakfast")) {
                response = sampleRecipe;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        if (method.equals("PUT")) {
            response = """
                    Fried Chicken and Egg Fried Rice
                    breakfast
                    Ingredients:
                    - 2 chicken breasts, diced
                    - 2 large eggs
                    - 2 cups cooked rice
                    - 2 tablespoons vegetable oil
                    - 2 tablespoons soy sauce
                    - 1 teaspoon sesame oil
                    - Salt and pepper to taste
                    Instructions:
                    1. Crack 2 eggs into bowl.
                    2. Have a shrimp fry the rice.
                    3. Enjoy!
                    """;
        }
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();

    }
}
