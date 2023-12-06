package code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import code.client.Model.*;
import code.client.View.*;
import code.client.Controllers.*;
import code.server.*;
import code.server.mocking.MockServer;
import java.net.ConnectException;
import java.net.MalformedURLException;

public class EndToEndScenario2_2 {
    BaseServer server = new MockServer(AppConfig.SERVER_HOST, AppConfig.SERVER_PORT);
    Model model = new Model();
    ResponseToRecipe format = new ResponseToRecipe();

    @Test
    public void serverUnavailableTest() throws MalformedURLException, IOException {
        String response = model.performAccountRequest("GET", "user", "password");
        assertTrue(response.contains("Error"));

        response = model.performRecipeRequest("GET", "recipe", "userId");
        assertTrue(response.contains("Error"));

        try {
            model.performWhisperRequest("GET", "mealType");
            assert (false);
        } catch (ConnectException e) {
            assert (true);
        }

        response = model.performChatGPTRequest("GET", "mealType", "ingredients");
        assertTrue(response.contains("Error"));

        response = model.performDallERequest("GET", "recipeTitle");
        assertTrue(response.contains("Error"));

    }

    @Test
    public void loginSuccessfulTest() {
    }

    @Test
    public void sortRecipeListTest() {
    }

    @Test
    public void filterRecipeListTest() {
    }

    @Test
    public void shareRecipeListTest() {

    }
}