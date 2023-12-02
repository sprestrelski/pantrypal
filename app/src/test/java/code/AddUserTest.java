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

    // create a new account if it doesn't exist
    @Test
    public void addUserTest() {
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> userCollection = mongoDb.getCollection(AppConfig.MONGO_USER_COLLECTION);
            AccountMongoDB accountDb = new AccountMongoDB(userCollection);
            Account account = new Account("Bob", "Ross");
            accountDb.add(account);
            Account test1 = accountDb.find("Bob");
            assertNotEquals(test1, null);
            accountDb.remove(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void confirmUserAndPasswordTest() {
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
            MongoCollection<Document> userCollection = mongoDb.getCollection(AppConfig.MONGO_USER_COLLECTION);
            AccountMongoDB accountDb = new AccountMongoDB(userCollection);

            // make an account
            Account account = new Account("Bob", "Ross");
            accountDb.add(account);
            Account test1 = accountDb.find("Bob");
            assertNotEquals(test1, null);

            // try to make an account with the same username
            Account temp1 = new Account("", "Robert");
            Account temp2 = new Account("Bob", "Robert");
            assertTrue(accountDb.validate("Bob", "Ross"));
            assertFalse(accountDb.add(temp1));
            assertFalse(accountDb.add(temp2));
            accountDb.remove(test1);
            accountDb.remove(temp1);
            accountDb.remove(temp2);
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
            Account test = accountDb.find("Greg Miranda");
            assertNull(test);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
