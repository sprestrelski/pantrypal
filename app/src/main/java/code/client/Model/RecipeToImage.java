package code.client.Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.bson.types.ObjectId;
import org.json.JSONObject;

public abstract class RecipeToImage {

    public abstract String getResponse(String recipeText)
            throws IOException, InterruptedException;

    public String parseResponseBody(String responseBody) {
        JSONObject responseJson = new JSONObject(responseBody);
        String generatedImageData = "";
        try {
            generatedImageData = responseJson.getJSONArray("data")
                    .getJSONObject(0).getString("b64_json");
        } catch (Exception e) {
            // badly formatted json
            generatedImageData = "An error occurred.";
            e.printStackTrace();
        }
        return generatedImageData;
    }

    public abstract byte[] downloadImageJSON(String generatedImageData, ObjectId id);

}
