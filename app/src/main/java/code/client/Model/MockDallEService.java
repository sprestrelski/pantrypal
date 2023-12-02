package code.client.Model;

import java.io.IOException;
import java.util.Base64;

import org.bson.types.ObjectId;

public class MockDallEService extends RecipeToImage {
  @Override
  public String getResponse(String recipeText) throws IOException, InterruptedException {
    return """
        {
          "created": 1701559231,
          "data": [
            {
              "b64_json": "RnJpZWQgUmljZSBJbWFnZSA6KQ=="
            }
          ]
        }
          """;
  }

  @Override
  public byte[] downloadImageJSON(String generatedImageData, ObjectId id) {
    byte[] generatedImageBytes = Base64.getDecoder().decode(generatedImageData);
    return generatedImageBytes;
  }

}
