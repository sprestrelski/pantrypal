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
            Account account = new Account("Bob", "Ross");
            accountDb.add(account);
            assertEquals(1, accountDb.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findUserTest() {
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> userCollection = mongoDb.getCollection(AppConfig.MONGO_USER_COLLECTION);
            IAccountDb accountDb = new AccountMongoDB(userCollection);
            Account test = accountDb.find("Bob");
            assertTrue(test != null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
