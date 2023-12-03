package code.client.Model;

import java.io.*;

public abstract class RecipeToImage {
    public abstract void setError(boolean error);

    public abstract String getResponse(String recipeTitle)
            throws IOException, InterruptedException;

    public abstract byte[] downloadImage(String generatedImageData, String id);

}
