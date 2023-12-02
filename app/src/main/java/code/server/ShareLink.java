package code.server;

public class ShareLink {
    private final String id;
    private final String senderId;
    private final String receiverId;
    private final String recipeId;

    public ShareLink(String id, String senderId, String receiverId, String recipeId) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.recipeId = recipeId;
    }

    public String getId() {
        return id;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ShareLink shareLink)) {
            return false;
        }
        return id.equals(shareLink.id);
    }
}
