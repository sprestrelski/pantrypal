package code;

import org.junit.jupiter.api.Test;

import code.client.Model.ITextToRecipe;
import code.client.Model.Recipe;
import code.client.Model.TextToRecipe;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateRecipeTest {
    /**
     * Unit Test for Create Recipe
     */
    @Test
    public void testCreateRecipe() {
        Recipe recipe = new Recipe("1", "Fried Rice");

        assertEquals("1", recipe.getId());
        assertEquals("Fried Rice", recipe.getTitle());
        recipe.addIngredient("Rice");
        recipe.addIngredient("Fried");
        recipe.addInstruction("A shrimp fried this rice?");

        String recipeString = recipe.toString();
        String parsedResponse = """
                Title: Fried Rice
                Ingredients:
                Rice
                Fried
                Instructions:
                A shrimp fried this rice?
                """;
        assertEquals(recipeString, parsedResponse);
    }

}