package code;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static org.junit.jupiter.api.Assertions.*;
import code.client.Model.AppConfig;
import code.server.AccountMongoDB;
import code.server.RecipeMongoDb;
import code.server.ShareRecipe;

import java.net.HttpURLConnection;
import java.net.URL;

public class ShareRecipeTest {
    // RecipeID to check 656d2d5d0cde2c34c86b7651 from lol
    @Test
    public void testShareLink() {
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> accountCollection = mongoDb.getCollection(AppConfig.MONGO_USER_COLLECTION);
            AccountMongoDB accountDb = new AccountMongoDB(accountCollection);
            MongoCollection<Document> recipeCollection = mongoDb.getCollection(AppConfig.MONGO_RECIPE_COLLECTION);
            RecipeMongoDb recipeDb = new RecipeMongoDb(recipeCollection);

            String shareLink = ShareRecipe.getSharedRecipe(accountDb, recipeDb, "lol", "656d2d5d0cde2c34c86b7651");
            URL url = new URL(shareLink);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            assertTrue(huc.getResponseMessage().length() > 50);
            if (huc.getResponseCode() != 404) {
                System.out.println("exists");
            } else {
                System.out.println("does not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNonShareLink() {
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> accountCollection = mongoDb.getCollection(AppConfig.MONGO_USER_COLLECTION);
            AccountMongoDB accountDb = new AccountMongoDB(accountCollection);
            MongoCollection<Document> recipeCollection = mongoDb.getCollection(AppConfig.MONGO_RECIPE_COLLECTION);
            RecipeMongoDb recipeDb = new RecipeMongoDb(recipeCollection);

            String shareLink = ShareRecipe.getSharedRecipe(accountDb, recipeDb, "lol", "nonExistent");
            URL url = new URL(shareLink);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            assertTrue(huc.getResponseMessage().length() < 50);
            if (huc.getResponseCode() != 404) {
                System.out.println("exists");
            } else {
                System.out.println("does not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}