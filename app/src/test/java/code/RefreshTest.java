package code;

import code.client.Model.Model;
import org.junit.jupiter.api.Test;
import code.client.Model.AppConfig;
import code.server.*;
import code.server.mocking.MockServer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.net.URISyntaxException;

public class RefreshTest {
    BaseServer server = new MockServer(AppConfig.SERVER_HOST, AppConfig.SERVER_PORT);
    Model model = new Model();

    @Test
    public void testRefreshRecipe() throws IOException, InterruptedException,
            URISyntaxException {
        server.start();
        String mealType = "breakfast";
        String ingredients = "chicken, eggs";
        String initialResponse = model.performChatGPTRequest("GET", mealType, ingredients);
        String expectedResponse = """
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
        assertEquals(expectedResponse, initialResponse);

        // simulate refresh
        String refreshResponse = model.performChatGPTRequest("GET2", mealType, ingredients);
        assertNotEquals(initialResponse, refreshResponse);
        server.stop();
    }

    // @Test
    // public void refreshTesting() {
    // DetailsAppFrame detailsAppFrame = new DetailsAppFrame();
    // // TextToRecipe txt = new TextToRecipe();
    // // initial recipe before refresh
    // Recipe initialRecipe = detailsAppFrame.getDisplayedRecipe();

    // MockGPTService mockGPT = new MockGPTService();
    // Recipe refreshedRecipe;
    // }

    // private Recipe generateRefresh(Recipe originalRecipe, MockGPTService
    // mockGPTService)
    // throws IOException, InterruptedException, URISyntaxException {
    // String mealType = "Breakfast";
    // String ingredients = "Chicken, eggs.";

    // String refreshedResponse = mockGPTService.getResponse(mealType, ingredients);
    // Recipe out = mockGPTService.mapResponseToRecipe(mealType, refreshedResponse);

    // return out;
    // }

}