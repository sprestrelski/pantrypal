package code.client.Controllers;

import java.io.IOException;
import java.net.URISyntaxException;

import code.client.Model.ITextToRecipe;
import code.client.Model.Recipe;

public class MockGPT implements ITextToRecipe {

    @Override
    public String buildPrompt(String typeOfMeal, String input) {
        return "I am a student on a budget with a busy schedule and I need to quickly cook a Lunch." +
                "I have chicken, and eggs. Make a recipe using only these ingredients plus condiments." +
                "Remember to first include a title, then a list of ingredients, and then a list of instructions.";
    }

    @Override
    public String getChatGPTResponse(String typeOfMeal, String input)
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
    public Recipe mapResponseToRecipe(String responseText) {
        Recipe recipe = new Recipe("1", "Fried Chicken");
        recipe.addIngredient("- 2 chicken breasts, diced");
        recipe.addIngredient("- 2 eggs");
        recipe.addInstruction("1. Crack 2 eggs into bowl.");
        recipe.addInstruction("2. Add chicken into bowl and then fry.");
        recipe.addInstruction("3. Enjoy!");
        return recipe;
    }

}
