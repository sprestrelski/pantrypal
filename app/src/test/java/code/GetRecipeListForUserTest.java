package code;

import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import code.server.*;
import code.client.Model.AppConfig;
import code.client.Model.ChatGPTService;
import code.client.Model.MockGPTService;
import code.client.Model.TextToRecipe;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class GetRecipeListForUserTest {
    private Account accountWithOne;
    private Account accountWithNone;
    private IRecipeDb recipeDb;

    @BeforeEach
    public void setUp() throws IOException {
        accountWithOne = new Account("656a2e6d8a659b00c86888b8", "chris", "chris");
        accountWithNone = new Account("6567dcbcb954ce6c58955ee0", "allen", "allen");
    }

    @Test
    public void findSingleRecipeStored() throws IOException, InterruptedException, URISyntaxException {
        TextToRecipe mockResponse = new MockGPTService();
        String storedRecipeStr = mockResponse.getResponse("", "");
        Recipe storedRecipe = mockResponse.mapResponseToRecipe("breakfast", storedRecipeStr);
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> recipeCollection = mongoDb.getCollection(AppConfig.MONGO_RECIPE_COLLECTION);
            recipeDb = new RecipeMongoDb(recipeCollection);
            List<Recipe> recipeList = recipeDb.getList(accountWithOne.getId());
            assertTrue(recipeList.size() == 1);
            assertEquals(storedRecipe, recipeList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findNoRecipeStored() {
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> recipeCollection = mongoDb.getCollection(AppConfig.MONGO_RECIPE_COLLECTION);
            recipeDb = new RecipeMongoDb(recipeCollection);
            List<Recipe> recipeList = recipeDb.getList(accountWithNone.getId());
            assertTrue(recipeList.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
