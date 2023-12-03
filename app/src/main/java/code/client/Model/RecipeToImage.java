package code.client.Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.bson.types.ObjectId;
import org.json.JSONObject;

public abstract class RecipeToImage {
    public abstract void setError(boolean error);

    public abstract String getResponse(String recipeTitle)
            throws IOException, InterruptedException;

    public abstract byte[] downloadImage(String generatedImageData, ObjectId id);

}
