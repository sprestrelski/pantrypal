package code;

import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import code.server.Account;
import code.client.Model.AccountCSVReader;
import code.client.Model.AccountCSVWriter;
import code.client.Model.AppConfig;
import code.client.View.*;
import code.client.Controllers.*;
import code.server.*;

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

    @BeforeAll
    public static void setUp() {
        account = new Account("Chef", "Caitlyn");
    }

    /**
     * Test that Chef Caitlyn was able to successfully create an account on MongoDB
     */
    @Test
    public void createAccountTest() {
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
    }

    /**
     * Test that Chef Caitlyn's user credentials were successfully saved onto her
     * device
     */
    @Test
    public void automaticLoginTest() throws IOException {
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
    }

    /**
     * Test that Chef Caitlyn can successfully create a recipe
     */
    @Test
    public void createRecipeTest() {

        // RecipeBuilder builder = new RecipeBuilder();

    }

    /**
     * Test that Chef Caitlyn can successfully regenerate a recipe if she doesn't
     * like it.
     */
    @Test
    public void refreshRecipeTest() {

    }

    /**
     * Test that Chef Caitlyn can successfully save a recipe
     */
    @Test
    public void saveRecipeTest() {
        RecipeBuilder builder = new RecipeBuilder(account.getId(), "Caitlyn's Lunch");
        builder.setMealTag("lunch");
        Recipe recipe = builder.buildRecipe();
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> recipeCollection = mongoDb.getCollection(AppConfig.MONGO_RECIPE_COLLECTION);
            RecipeMongoDb recipeDB = new RecipeMongoDb(recipeCollection);
            recipeDB.add(recipe);
            Recipe receivedRecipe = recipeDB.find(recipe.getId());
            assertTrue(receivedRecipe != null);
            assertTrue(receivedRecipe.getAccountId().contains(account.getId()));
            recipeDB.remove(recipe.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}