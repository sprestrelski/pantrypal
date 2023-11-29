package code;

import code.server.Account;
import code.client.Model.AppConfig;
import code.server.AccountMongoDB;
import code.server.IAccountDb;
import code.client.Model.*;

import static org.junit.jupiter.api.Assertions.*;

import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Whatever {
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> collection = mongoDb.getCollection(AppConfig.MONGO_RECIPE_COLLECTION);
            IRecipeDb recipeDb = new RecipeMongoDb(collection);
            Recipe recipe = new Recipe("Sample recipe");
            recipe.addIngredient("sample ingredient");
            recipe.addInstruction("sample instruction");
            recipeDb.add(recipe);
            while (true)
                ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}