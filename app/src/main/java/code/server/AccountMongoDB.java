package code.server;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
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
        return account;
    }

    @Override
    public Account find(String username) {
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
    public boolean validate(String username, String password) {
        Account account = find(username);
        if (account == null) {
            return false;
        }
        return account.getPassword().equals(password);
    }

    @Override
    public boolean add(Account account) {
        // if (find(account.getUsername()) == null) {
        // return false;
        // }

        Document accountDocument = new Document("_id", account.getId());
        accountDocument.append("username", account.getUsername()).append("password", account.getPassword());
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
    public Account find(ObjectId id) {
        Bson filter = eq("_id", id);
        var accountDocumentIter = accountDocumentCollection.find(filter);
        Document accountDocument = accountDocumentIter.first();
        if (accountDocument == null) {
            // Account does not exist
            return null;
        }
        return jsonToAccount(accountDocument);
    }

    @Override
    public boolean update(Account updatedAccount) {
        Account oldAccount = remove(updatedAccount.getId());
        if (oldAccount == null) {
            // Account does not exist
            return false;
        }
        add(updatedAccount);
        return true;
    }

    @Override
    public Account remove(ObjectId id) {
        Bson filter = eq("_id", id);
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
