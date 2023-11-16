package code.client.Model;

import java.io.IOException;
import java.net.URISyntaxException;

public class MockGPT implements ITextToRecipe {

    @Override
    public String buildPrompt(String mealType, String ingredients) {
        return "Mock prompt";
    }

    @Override
    public String getChatGPTResponse(String mealType, String ingredients)
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

    @Override
    public Recipe mapResponseToRecipe(String mealType, String responseText) {
        Recipe recipe = new Recipe("Fried Chicken");
        recipe.addIngredient("2 chicken breasts, diced");
        recipe.addIngredient("2 eggs");
        recipe.addInstruction("Crack 2 eggs into bowl.");
        recipe.addInstruction("Add chicken into bowl and then fry.");
        recipe.addInstruction("Enjoy!");
        return recipe;
    }

}
