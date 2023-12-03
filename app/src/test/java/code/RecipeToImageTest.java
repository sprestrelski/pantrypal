package code;

import org.junit.jupiter.api.Test;

import code.client.Model.Recipe;
import code.client.Model.RecipeToImage;
import code.client.Model.MockDallEService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RecipeToImageTest {
    /*
     * Image creation unit tests
     */
    @Test
    public void testImageCreation() throws IOException, InterruptedException {
        // create a recipe
        Recipe recipe = new Recipe("Fried Rice", "Lunch");
        assertEquals("Fried Rice", recipe.getTitle());
        recipe.addIngredient("Rice");
        recipe.addIngredient("Fried");
        recipe.addInstruction("A shrimp fried this rice?");

        // DallE request
        RecipeToImage recipeToImage = new MockDallEService();
        String imageString = recipeToImage.getResponse(recipe.getTitle());
        // parse base64 to image
        byte[] imageBytes = recipeToImage.downloadImage(imageString, recipe.getId());
        String expectedResponse = "Fried Rice Image :)";
        assertEquals(expectedResponse, new String(imageBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testImageCreationError() throws IOException, InterruptedException {
        // create a recipe
        Recipe recipe = new Recipe("Fried Rice", "Lunch");
        assertEquals("Fried Rice", recipe.getTitle());
        recipe.addIngredient("Rice");
        recipe.addIngredient("Fried");
        recipe.addInstruction("A shrimp fried this rice?");

        // DallE request
        RecipeToImage recipeToImage = new MockDallEService();
        recipeToImage.setError(true);
        String imageString = recipeToImage.getResponse(recipe.getTitle());
        assertEquals("error", imageString);
    }

}