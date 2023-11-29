package code.server;

import java.util.List;
import org.bson.types.ObjectId;

public interface IAccountDb {
    List<Account> getList();
    boolean validate(String username, String password);
    boolean add(Account account);
    Account find(ObjectId id);
    Account find(String username);
    boolean update(Account updatedAccount);
    Account remove(ObjectId id);
    void clear();
    int size();
}