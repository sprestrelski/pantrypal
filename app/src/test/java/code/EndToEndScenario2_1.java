package code;

import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.net.URISyntaxException;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.List;

import code.client.Model.*;
import code.client.Model.AccountCSVWriter;
import code.client.Model.AppConfig;
import code.client.Model.Model;
import code.server.*;
import code.server.Account;
import code.server.mocking.MockServer;
import code.server.mocking.MockChatGPTRequestHandler;

/**
 * This test file covers the End-to-End Scenario in which Chef Caitlyn:
 * 1. Creates an account
 * 2. Opts for automatic login
 * 3. Creates a new recipe
 * 4. Refreshes the new recipe
 * 5. Saves the refreshed recipe
 */
public class EndToEndScenario2_1 {
    private static Account account; // Account used in the following tests
    private static BaseServer server; // Mock server used in the following tests
    private static Model model; // Model used in the following tests

    @BeforeAll
    public static void setUp() throws IOException {
        // Initialize an account for Chef Caitlyn
        account = new Account("Chef", "Caitlyn");
        // Initialize a mocked server that PantryPal will "use"
        server = new MockServer("localhost", AppConfig.SERVER_PORT);
        // Initialize a helper model object
        model = new Model();
        // Start up the server before Chef Caitlyn opens up the app
    }

    /**
     * Test that Chef Caitlyn was able to successfully create an account on MongoDB.
     */
    @Test
    public void createAccountTest() {
        try {
            server.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> accountCollection = mongoDb.getCollection(AppConfig.MONGO_USER_COLLECTION);
            AccountMongoDB accountDb = new AccountMongoDB(accountCollection);
            accountDb.add(account);
            assertTrue(accountDb.findByUsername("Chef") != null);
            accountDb.removeByUsername("Chef");
        } catch (Exception e) {
            e.printStackTrace();
        }
        server.stop();
    }

    /**
     * Test that Chef Caitlyn's user credentials were successfully saved onto her
     * device.
     */
    @Test
    public void automaticLoginTest() throws IOException {
        try {
            server.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Save the username and password to a file called "UserCredentialsTest.csv"
        AccountCSVWriter writer = new AccountCSVWriter(new FileWriter("UserCredentialsTest.csv"));
        writer.writeAccount(account.getUsername(), account.getPassword());
        writer.close();
        // Read the username and password from a file called "UserCredentialsTest.csv"
        AccountCSVReader reader = new AccountCSVReader(new FileReader("UserCredentialsTest.csv"));
        List<String> userCredentials = reader.readUserCredentials();
        reader.close();
        // Check that the username and password were correctly saved to the csv file
        String expectedUsername = "Chef";
        String expectedPassword = "Caitlyn";
        assertEquals(expectedUsername, userCredentials.get(0));
        assertEquals(expectedPassword, userCredentials.get(1));
        server.stop();
    }

    /**
     * Test that Chef Caitlyn can successfully generate a recipe after logging in.
     */
    @Test
    public void createRecipeTest() {
        try {
            server.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Perform a mock ChatGPT request for Chef Caitlyn's fried chicken recipe
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
        // Check that the recipe was created successfully from the ChatGPT response
        assertEquals(expectedResponse, initialResponse);
        server.stop();
    }

    /**
     * Test that Chef Caitlyn can successfully regenerate a recipe if she doesn't
     * like it.
     */
    @Test
    public void refreshRecipeTest() {
        try {
            server.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /* START OF COPIED TEST CONTENT */

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

        /* END OF COPIED TEST CONTENT */

        // Mock a recipe refresh on the recipe created in the previous test
        String refreshResponse = model.performChatGPTRequest("PUT", mealType, ingredients);
        // Check that the recipe body is no longer the same
        assertNotEquals(initialResponse, refreshResponse);
        server.stop();
    }

    /**
     * Test that Chef Caitlyn can successfully save a recipe to MongoDB.
     */
    @Test
    public void saveRecipeTest() {
        try {
            server.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Build a recipe based on the mocked refreshed recipe from the previous test
        RecipeBuilder builder = new RecipeBuilder(account.getId(), "Fried Chicken and Egg Fried Rice");
        builder.setMealTag("breakfast");
        Recipe recipe = builder.buildRecipe();
        // Add the ingredients of the "refreshed" recipe
        recipe.addIngredient("- 2 chicken breasts, diced");
        recipe.addIngredient("- 2 large eggs");
        recipe.addIngredient("- 2 cups cooked rice");
        recipe.addIngredient("- 2 tablespoons vegetable oil");
        recipe.addIngredient("- 2 tablespoons soy sauce");
        recipe.addIngredient("- 1 teaspoon sesame oil");
        recipe.addIngredient("- Salt and pepper to taste");
        // Add the instructions of the "refreshed" recipe
        recipe.addInstruction("1. Crack 2 eggs into bowl.");
        recipe.addInstruction("2. Have a shrimp fry the rice.");
        recipe.addInstruction("3. Enjoy!");
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> recipeCollection = mongoDb.getCollection(AppConfig.MONGO_RECIPE_COLLECTION);
            RecipeMongoDb recipeDB = new RecipeMongoDb(recipeCollection);
            recipeDB.add(recipe);
            Recipe receivedRecipe = recipeDB.find(recipe.getId());
            // Check that the recipe was saved to the MongoDB
            assertTrue(receivedRecipe != null);
            // Check that the recipe was saved to Chef Caitlyn's account
            assertTrue(receivedRecipe.getAccountId().contains(account.getId()));
            recipeDB.remove(recipe.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Stop the server once Chef Caitlyn is done using the app
        server.stop();
    }
}