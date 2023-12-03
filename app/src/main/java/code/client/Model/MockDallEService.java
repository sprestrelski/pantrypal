package code.client.Model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import org.bson.types.ObjectId;

public class MockDallEService extends RecipeToImage {
  private boolean error = false;

  @Override
  public void setError(boolean error) {
    this.error = error;
  }

  @Override
  public String getResponse(String recipeText) throws IOException, InterruptedException {
    if (this.error) {
      return "error";
    } else {
      File file = new File(AppConfig.RECIPE_IMG_FILE);
      // try to give default image
      try {
        byte[] imageBytes = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(imageBytes);
      } catch (Exception fileError) {
        fileError.printStackTrace();
        return "RnJpZWQgUmljZSBJbWFnZSA6KQ==";
      }
    }
  }

  @Override
  public byte[] downloadImage(String generatedImageData, ObjectId id) {
    byte[] generatedImageBytes = Base64.getDecoder().decode(generatedImageData);
    return generatedImageBytes;
  }

}
