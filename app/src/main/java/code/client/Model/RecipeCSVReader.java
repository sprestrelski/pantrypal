package code.client.Model;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;
import org.bson.types.ObjectId;

public class RecipeCSVReader {
    private final BufferedReader buffReader;

    public RecipeCSVReader(Reader reader) {
        buffReader = new BufferedReader(reader);
    }

    private Recipe readRecipe(String recipeStr) throws IOException {
        String[] recipeTokens = recipeStr.split("::");
        String id = recipeTokens[0];
        String accountId = recipeTokens[1];
        String title = recipeTokens[2];
        String mealTag = recipeTokens[3];
        String[] ingredientTokens = recipeTokens[4].split(";;");
        String[] instructionTokens = recipeTokens[5].split(";;");
        String image = recipeTokens[6];
        Recipe recipe = new Recipe(new ObjectId(id), new ObjectId(accountId), title, mealTag, image);
        // error occurred, images have a pretty long bytestring
        if (image.length() < 256) {
            recipe.setDefaultImage();
        }

        for (String ingredient : ingredientTokens) {
            recipe.addIngredient(ingredient);
        }

        for (String instruction : instructionTokens) {
            recipe.addInstruction(instruction);
        }

        return recipe;
    }

    public Recipe readRecipe() throws IOException {
        String recipeStr = buffReader.readLine();
        return readRecipe(recipeStr);
    }

    public void readRecipeDb(IRecipeDb recipeDb) throws IOException {
        String line;
        buffReader.readLine(); // skip the line with the delimeter specifier
        buffReader.readLine(); // skip the line with the csv column labels
        Recipe recipe;

        while ((line = buffReader.readLine()) != null) {
            recipe = readRecipe(line);
            recipeDb.add(recipe);
        }
    }

    public void close() throws IOException {
        buffReader.close();
    }
}