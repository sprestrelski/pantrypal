package code;

import org.junit.jupiter.api.Test;

import code.server.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateRecipeTest {
    /**
     * Unit Test for Create Recipe
     */
    @Test
    public void testCreateRecipe() {
        RecipeBuilder builder = new RecipeBuilder("656a2e6d8a659b00c86888b8", "Fried Rice");
        builder.setMealTag("Lunch");
        Recipe recipe = builder.buildRecipe();
        assertEquals("Fried Rice", recipe.getTitle());
        recipe.addIngredient("Rice");
        recipe.addIngredient("Fried");
        recipe.addInstruction("A shrimp fried this rice?");
        String recipeString = recipe.toString();
        String parsedResponse = """
                Title: Fried Rice
                Meal tag: Lunch
                Ingredients:
                Rice
                Fried
                Instructions:
                A shrimp fried this rice?
                """;
        assertEquals(parsedResponse, recipeString);
    }
}
