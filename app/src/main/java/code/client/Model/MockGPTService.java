package code.client.Model;

import java.io.IOException;
import java.net.URISyntaxException;

public class MockGPTService extends TextToRecipe {
    @Override
    public String getResponse(String mealType, String ingredients)
            throws IOException, InterruptedException, URISyntaxException {
        return """
                Fried Chicken
                Ingredients:
                - 2 chicken breasts, diced
                - 2 eggs
                Instructions:
                1. Crack 2 eggs into bowl.
                2. Add chicken into bowl and then fry.
                3. Enjoy!
                """;
    }
}
