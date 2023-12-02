package code.server;

import java.util.List;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;

public class ShareLinkMongoDb implements IShareLinkDb {
    private MongoCollection<Document> shareLinkDocumentCollection;

    public ShareLinkMongoDb(MongoCollection<Document> shareLinkDocumentCollection) {
        this.shareLinkDocumentCollection = shareLinkDocumentCollection;
    }

    private ShareLink jsonToShareLink(Document shareLinkDocument) {
        // Gson gson = new Gson();
        // ShareLink shareLink = gson.fromJson(shareLinkDocument.toJson(), ShareLink.class);
        // JsonObject jsonObj = JsonParser.parseString(shareLinkDocument.toJson().toString()).getAsJsonObject();
        // String shareLinkId = jsonObj.getAsJsonObject("_id").get("$oid").getAsString();
        // shareLink.setId(shareLinkId);
        // return shareLink;
        return null;
    }

    @Override
    public List<String> getRecipeIds(String receiverId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRecipeIds'");
    }

    @Override
    public List<String> getRecipeIds(String senderId, String receiverId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRecipeIds'");
    }
    
}
