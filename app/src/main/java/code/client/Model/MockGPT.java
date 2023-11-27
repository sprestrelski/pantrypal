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

                2 chicken breasts, diced
                2 eggs

                Instructions:
                Crack 2 eggs into bowl.
                Add chicken into bowl and then fry.
                Enjoy!
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
