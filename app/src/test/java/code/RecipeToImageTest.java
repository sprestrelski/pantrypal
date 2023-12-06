package code;

import org.junit.jupiter.api.Test;

import code.server.BaseServer;
import code.server.Recipe;
import code.server.mocking.MockServer;
import code.client.Model.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Base64;

public class RecipeToImageTest {
    private final BaseServer server = new MockServer(AppConfig.SERVER_HOST, AppConfig.SERVER_PORT);
    private final Model model = new Model();

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
        server.start();
        String imageString = model.performDallERequest("GET", recipe.getTitle());
        imageString = new String(Base64.getDecoder().decode(imageString));

        // default image
        String expectedResponse = "Fried Rice Image";

        assertEquals(expectedResponse, imageString);
        server.stop();
    }

}