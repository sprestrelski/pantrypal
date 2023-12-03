package code.client.Model;

import java.io.IOException;
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
      return "RnJpZWQgUmljZSBJbWFnZSA6KQ==";
    }
  }

  @Override
  public byte[] downloadImage(String generatedImageData, ObjectId id) {
    byte[] generatedImageBytes = Base64.getDecoder().decode(generatedImageData);
    return generatedImageBytes;
  }

}
