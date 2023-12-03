package code.client.Model;

import java.io.IOException;
import java.net.URISyntaxException;

public class MockGPTService extends TextToRecipe {

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

    public void setSampleRecipe(String recipeText) {
        sampleRecipe = recipeText;
    }

    @Override
    public String getResponse(String mealType, String ingredients)
            throws IOException, InterruptedException, URISyntaxException {
        return sampleRecipe;
    }
}
