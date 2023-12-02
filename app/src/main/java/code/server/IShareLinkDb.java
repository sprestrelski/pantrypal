package code.server;

import java.util.List;

public interface IShareLinkDb {
    List<String> getRecipeIds(String receiverId);
    List<String> getRecipeIds(String senderId, String receiverId);
}
