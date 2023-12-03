package code;

import org.junit.jupiter.api.Test;

import code.server.Recipe;
import code.client.Model.RecipeToImage;
import code.client.Model.AppConfig;
import code.client.Model.MockDallEService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

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

        // default image
        File file = new File(AppConfig.RECIPE_IMG_FILE);
        String expectedResponse = "Fried Rice Image :)";
        try {
            byte[] defaultImageBytes = Files.readAllBytes(file.toPath());
            expectedResponse = Base64.getEncoder().encodeToString(defaultImageBytes);
        } catch (Exception fileError) {
            fileError.printStackTrace();
        }

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