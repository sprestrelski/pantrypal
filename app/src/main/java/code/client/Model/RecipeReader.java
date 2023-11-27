package code.client.Model;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class RecipeReader {
    private final BufferedReader buffReader;

    public RecipeReader(Reader reader) {
        buffReader = new BufferedReader(reader);
    }

    private Recipe readRecipe(String recipeStr) throws IOException {
        String[] recipeTokens = recipeStr.split("::");
        // System.out.println("Reading recipe: " + recipeTokens[0]);
        String[] ingredientTokens = recipeTokens[1].split(";;");
        String[] instructionTokens = recipeTokens[2].split(";;");
        Recipe recipe = new Recipe(recipeTokens[0]);
        recipe.setID(UUID.nameUUIDFromBytes(recipeTokens[0].getBytes()));

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

    public IRecipeDb readRecipeDb() throws IOException {
        String line;
        buffReader.readLine(); // skip the line with the delimeter specifier
        buffReader.readLine(); // skip the line with the csv column labels
        IRecipeDb recipeDb = new RecipeDb();
        Recipe recipe;

        while ((line = buffReader.readLine()) != null) {
            recipe = readRecipe(line);
            recipeDb.add(recipe);
        }

        return recipeDb;
    }
}