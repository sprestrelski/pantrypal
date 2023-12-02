package code.server;

import java.util.List;

public interface IAccountDb {
    List<Account> getList();
    boolean checkPassword(String username, String password);
    boolean add(Account account);
    Account findById(String id);
    Account findByUsername(String username);
    boolean update(Account updatedAccount);
    Account removeById(String id);
    Account removeByUsername(String username);
    void clear();
    int size();
}