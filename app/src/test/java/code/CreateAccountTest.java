package code;

import code.server.Account;
import code.server.AccountMongoDB;
import code.server.IAccountDb;
import static org.junit.jupiter.api.Assertions.*;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class CreateAccountTest {

    // create a new account if it doesn't exist
    @Test
    public void testCreateAccount() {
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> accountCollection = mongoDb.getCollection(AppConfig.MONGO_USER_COLLECTION);
            AccountMongoDB accountDb = new AccountMongoDB(accountCollection);
            Account account1 = new Account("Bob", "Ross");
            accountDb.add(account1);
            Account account2 = accountDb.findByUsername("Bob");
            assertNotEquals(account2, null);
            accountDb.removeByUsername(account1.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConfirmAccount() {
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> accountCollection = mongoDb.getCollection(AppConfig.MONGO_USER_COLLECTION);
            AccountMongoDB accountDb = new AccountMongoDB(accountCollection);
            Account account1 = new Account("", "Robert");
            Account account2 = new Account("Bob", "Robert");
            assertTrue(accountDb.checkPassword("Bob", "Ross"));
            assertFalse(accountDb.add(account1));
            assertFalse(accountDb.add(account2));
            accountDb.removeByUsername(account1.getUsername());
            accountDb.removeByUsername(account2.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindAccount() {
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> accountCollection = mongoDb.getCollection(AppConfig.MONGO_USER_COLLECTION);
            IAccountDb accountDb = new AccountMongoDB(accountCollection);
            Account account = accountDb.findByUsername("Greg Miranda");
            assertNull(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
