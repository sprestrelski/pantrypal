package code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import code.client.Controllers.Format;
import code.client.Model.AccountCSVReader;
import code.client.Model.AccountCSVWriter;
import code.client.Model.AppConfig;
import code.client.Model.Model;
import code.client.Model.RecipeSorter;
import code.server.Account;
import code.server.AccountMongoDB;
import code.server.BaseServer;
import code.server.Recipe;
import code.server.RecipeBuilder;
import code.server.RecipeMongoDb;
import code.server.ShareRecipe;
import code.server.mocking.MockServer;

/**
 * This test file covers the End-to-End Scenario in which Chef Caitlyn:
 * 1. Encounters a glitch where the server is temporarily unavailable
 * 2. Sucessfully logs into the app once the server comes back online
 * 3. Successfully sorts her recipe list in alphabetical and chronological order
 * 4. Successfully filters her recipes to contain only lunch recipes
 * 5. Successfully shares one of the recipes from her recipe list
 */
public class EndToEndScenario2_2 {
    Format format = new Format();
    private AccountCSVWriter writer; // Writer that saves user credentials to a csv file
    private AccountCSVReader reader; // Reader that reads user credentials from a csv file
    private List<String> userCredentials; // Helper variable that stores credentials read from the csv file
    private static Account account; // Account used in the following tests
    private static BaseServer server; // Mock server used in the following tests
    private static Model model; // Model used in the following tests
    private static RecipeBuilder b1, b2, b3, b4; // Recipe builders used to set up the recipe list
    private static Recipe r1, r2, r3, r4; // Recipes that will be used in the following tests
    private static List<Recipe> initialRecipeList; // Main recipe list used throughout the tests

    /**
     * This method is a replica of our update method in RecipeListUI. It has the
     * same logic, but is rewritten to not create UI elements. Additionally, it
     * adds the elements to a normal List rather than a UI list.
     * 
     * @param filter - the filter criteria selected by the user
     */
    private void update(String filter) {
        List<Recipe> temp = new ArrayList<>();
        for (Recipe recipe : initialRecipeList) {
            if (filter.equals("none") || recipe.getMealTag().toLowerCase().equals(filter.toLowerCase())) {
                temp.add(recipe);
            }
        }
        initialRecipeList = temp;
    }

    @BeforeAll
    public static void setUp() throws IOException {
        // Initialize an account for Chef Caitlyn
        account = new Account("Chef", "Caitlyn");
        // Initialize a mocked server that PantryPal will "use"
        server = new MockServer("localhost", AppConfig.SERVER_PORT);
        // Initialize a helper model object
        model = new Model();
        // Start up the server before Chef Caitlyn opens up the app
        server.start();
        // Initialize an empty recipe list
        initialRecipeList = new ArrayList<>();
        // Initialize recipe builders that specify only the title and date
        b1 = new RecipeBuilder(account.getId(), "Recipe One"); // Alphabetically 2nd, Chronologically 2nd
        b1.setDate(200);
        b1.setMealTag("lunch");
        b2 = new RecipeBuilder(account.getId(), "Recipe Two"); // Alphabetically 4th, Chronologicaly 4th
        b2.setDate(400);
        b2.setMealTag("breakfast");
        b3 = new RecipeBuilder(account.getId(), "Recipe Three"); // Alphabetically 3rd, Chronologically 1st
        b3.setDate(50);
        b3.setMealTag("lunch");
        b4 = new RecipeBuilder(account.getId(), "Recipe Four"); // Alphabetically 1st, Chronologically 3rd
        b4.setDate(300);
        b4.setMealTag("dinner");
        // Use the recipe builders to build the recipes
        r1 = b1.buildRecipe();
        r2 = b2.buildRecipe();
        r3 = b3.buildRecipe();
        r4 = b4.buildRecipe();
        // Add the recipes to the recipe list in the order r1, r2, r3, r4
        initialRecipeList.add(r1);
        initialRecipeList.add(r2);
        initialRecipeList.add(r3);
        initialRecipeList.add(r4);
        server.stop();
    }

    @Test
    public void serverUnavailableTest() throws MalformedURLException, IOException {
        server.stop();
        String response = model.performAccountRequest("GET", "user", "password");
        assertTrue(response.contains("Error"));

        response = model.performRecipeRequest("GET", "recipe", "userId");
        assertTrue(response.contains("Error"));

        response = model.performWhisperRequest("GET", "wah");
        assertTrue(response.contains("Error"));

        response = model.performChatGPTRequest("GET", "mealType", "ingredients");
        assertTrue(response.contains("Error"));

        response = model.performDallERequest("GET", "recipeTitle");
        assertTrue(response.contains("Error"));

    }

    @Test
    public void loginSuccessfulTest() {
        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Server failed to start: " + e.getMessage());
        }
        String successMessage = "success";
        userCredentials = new ArrayList<>();

        try {
            writer = new AccountCSVWriter(new FileWriter("UserCredentialsTest.csv"));
            writer.writeAccount(account.getUsername(), account.getPassword());
            writer.close();

            reader = new AccountCSVReader(new FileReader("UserCredentialsTest.csv"));
            userCredentials = reader.readUserCredentials();
            reader.close();

            assertTrue(userCredentials.size() == 2);
            assertTrue(userCredentials.get(0).equals(account.getUsername()));
            assertTrue(userCredentials.get(1).equals(account.getPassword()));

            try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
                MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
                MongoCollection<Document> accountCollection = mongoDb.getCollection(AppConfig.MONGO_USER_COLLECTION);
                AccountMongoDB accountDb = new AccountMongoDB(accountCollection);
                accountDb.add(account);
                String loginResp = model.performAccountRequest("GET", account.getUsername(), account.getPassword());
                assertNotEquals("Username is not found", loginResp);
                assertNotEquals("Incorrect password", loginResp);
                accountDb.removeByUsername("Chef");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed test setup");
        }
        server.stop();
    }

    /**
     * Test that Chef Caitlyn can successfully sort her recipe list in alphabetical
     * and chronological order.
     */
    @Test
    public void sortRecipeListTest() {
        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Server failed to start: " + e.getMessage());
        }

        // String loginResponse = model.performAccountRequest("POST", "user",
        // "password");
        // assertTrue(loginResponse.contains("success"), "Login unsuccessful");

        // Initialize a recipe list sorter for the empty recipe list
        RecipeSorter sorter = new RecipeSorter(initialRecipeList);
        // Chef Caitlyn tries sorting her recipe list from oldest to newest
        sorter.sortOldestToNewest();
        // Check that Chef Caitlyn's list is now in oldest to newest order
        assertEquals(r3.getDate(), initialRecipeList.get(0).getDate());
        assertEquals(r1.getDate(), initialRecipeList.get(1).getDate());
        assertEquals(r4.getDate(), initialRecipeList.get(2).getDate());
        assertEquals(r2.getDate(), initialRecipeList.get(3).getDate());
        // Chef Caitlyn tries sorting her recipe list in alphabetical order
        sorter.sortAToZ();
        // Check that Chef Caitlyn's list is now in alphabetical order
        assertEquals(r4.getTitle(), initialRecipeList.get(0).getTitle());
        assertEquals(r1.getTitle(), initialRecipeList.get(1).getTitle());
        assertEquals(r3.getTitle(), initialRecipeList.get(2).getTitle());
        assertEquals(r2.getTitle(), initialRecipeList.get(3).getTitle());
        server.stop();
    }

    /**
     * Test that Chef Caitlyn can successfully filter her recipe list to only show
     * lunch recipes.
     */
    @Test
    public void filterRecipeListTest() {
        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Server failed to start: " + e.getMessage());
        }
        // Recipe list size should be 4 before filtering
        assertTrue(initialRecipeList.size() == 4);
        // Filter the recipe list to show only lunch recipes
        update("lunch");
        // Filtered recipe list should only have r1 and r3
        List<Recipe> expected = new ArrayList<>();
        expected.add(r1);
        expected.add(r3);
        assertEquals(expected, initialRecipeList);
        // Recipe list size should be 2 after filtering
        assertTrue(initialRecipeList.size() == 2);
        server.stop();
    }

    @Test
    public void shareRecipeListTest() {
        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Server failed to start: " + e.getMessage());
        }
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> accountCollection = mongoDb.getCollection(AppConfig.MONGO_USER_COLLECTION);
            AccountMongoDB accountDb = new AccountMongoDB(accountCollection);
            MongoCollection<Document> recipeCollection = mongoDb.getCollection(AppConfig.MONGO_RECIPE_COLLECTION);
            RecipeMongoDb recipeDb = new RecipeMongoDb(recipeCollection);

            String query = "localhost:8100/recipes/lol/656eb76baa1a68349d0cc61d";
            int usernameStart = query.indexOf(AppConfig.SHARE_PATH);
            String username = query.substring(usernameStart + AppConfig.SHARE_PATH.length());
            String recipeID = username.substring(username.indexOf("/") + 1);
            username = username.substring(0, username.indexOf("/"));
            String response = ShareRecipe.getSharedRecipe(accountDb, recipeDb, username, recipeID);
            String expected = "";
            /*
             * title: "Coconut Mango Sticky Rice Bowl"
             * mealTag: "Lunch"
             */
            // Ingredients:
            // 1 cup coconut milk
            // 1 cup mango pieces
            // 2 cups cooked sticky rice
            // 1 tablespoon soy sauce
            // 2 teaspoons vegetable oil
            // 1 teaspoon sesame oil
            // another cup of coconut milk
            // Instructions:
            // Once the oil is hot, add the mango pieces and cook for 2 minutes, stirring
            // frequently.
            // Add the soy sauce and salt to taste, and cook for another 2 minutes.
            // Add the coconut milk and cooked sticky rice and cook for 3-4 minutes,
            // stirring frequently.
            assertTrue(response.contains("Coconut Mango Sticky Rice Bowl"));
            assertTrue(response.contains("1 cup mango pieces"));
            assertTrue(response.contains("Add the coconut"));
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Stop the server once Chef Caitlyn is done using the app
        server.stop();
    }
}