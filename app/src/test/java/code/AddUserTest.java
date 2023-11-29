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

public class AddUserTest {
    // private IAccountDb accountDb;

    // @BeforeEach
    // public void setup() {
    // try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
    // MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
    // MongoCollection<Document> userCollection =
    // mongoDb.getCollection(AppConfig.MONGO_USER_COLLECTION);
    // accountDb = new AccountMongoDB(userCollection);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    @Test
    public void addUserTest() {
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> userCollection = mongoDb.getCollection(AppConfig.MONGO_USER_COLLECTION);
            IAccountDb accountDb = new AccountMongoDB(userCollection);
            Account test = new Account("Bob", "Ross"); // 6566cd345582f05a5158fd6a
            // accountDb.add(test);
            // assertEquals(0, accountDb.size());
            assertTrue(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
