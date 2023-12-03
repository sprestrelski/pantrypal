package code.server;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.eq;

import java.util.List;
import java.util.ArrayList;

public class AccountMongoDB implements IAccountDb {
    private MongoCollection<Document> accountDocumentCollection;

    public AccountMongoDB(MongoCollection<Document> accountDocumentCollection) {
        this.accountDocumentCollection = accountDocumentCollection;
    }

    private Account jsonToAccount(Document accountDocument) {
        Gson gson = new Gson();
        Account account = gson.fromJson(accountDocument.toJson(), Account.class);
        JsonObject jsonObj = JsonParser.parseString(accountDocument.toJson().toString()).getAsJsonObject();
        String accountId = jsonObj.getAsJsonObject("_id").get("$oid").getAsString();
        account.setId(accountId);
        return account;
    }

    @Override
    public Account findByUsername(String username) {
        Bson filter = eq("username", username);
        var accountDocumentIter = accountDocumentCollection.find(filter);
        Document accountDocument = accountDocumentIter.first();
        if (accountDocument == null) {
            // Account does not exist
            return null;
        }
        return jsonToAccount(accountDocument);
    }

    @Override
    public Account findById(String id) {
        Bson filter = eq("_id", new ObjectId(id));
        var accountDocumentIter = accountDocumentCollection.find(filter);
        Document accountDocument = accountDocumentIter.first();
        if (accountDocument == null) {
            // Account does not exist
            return null;
        }
        return jsonToAccount(accountDocument);
    }

    // This method assumes that the username exists
    @Override
    public boolean checkPassword(String username, String password) {
        Account account = findByUsername(username);
        return account.getPassword().equals(password);
    }

    @Override
    public boolean add(Account account) {
        String username = account.getUsername();
        if (findByUsername(username) != null || username.isEmpty()) {
            return false;
        }

        Document accountDocument = new Document("_id", new ObjectId(account.getId()));
        accountDocument.append("username", username)
                .append("password", account.getPassword());
        accountDocumentCollection.insertOne(accountDocument);
        return true;
    }

    @Override
    public List<Account> getList() {
        Account account;
        Document accountDocument;
        var accountDocumentCursor = accountDocumentCollection.find().cursor();
        List<Account> accountList = new ArrayList<>();

        while (accountDocumentCursor.hasNext()) {
            accountDocument = accountDocumentCursor.next();
            account = jsonToAccount(accountDocument);
            accountList.add(account);
        }

        return accountList;
    }

    @Override
    public boolean update(Account updatedAccount) {
        Account oldAccount = removeById(updatedAccount.getId());
        if (oldAccount == null) {
            // Account does not exist
            return false;
        }
        add(updatedAccount);
        return true;
    }

    @Override
    public Account removeById(String id) {
        Bson filter = eq("_id", new ObjectId(id));
        Document accountDocument = accountDocumentCollection.findOneAndDelete(filter);
        if (accountDocument == null) {
            // Account does not exist
            return null;
        }
        return jsonToAccount(accountDocument);
    }

    @Override
    public Account removeByUsername(String username) {
        Bson filter = eq("username", username);
        Document accountDocument = accountDocumentCollection.findOneAndDelete(filter);
        if (accountDocument == null) {
            // Account does not exist
            return null;
        }
        return jsonToAccount(accountDocument);
    }

    @Override
    public void clear() {
        accountDocumentCollection.drop();
    }

    @Override
    public int size() {
        return (int) accountDocumentCollection.countDocuments();
    }
}
