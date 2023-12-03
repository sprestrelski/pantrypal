package code.client.Model;

import java.io.Writer;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import code.server.Recipe;
import code.server.IRecipeDb;

public class RecipeCSVWriter {
    private final Writer writer;

    public RecipeCSVWriter(Writer writer) {
        this.writer = writer;
    }

    public void writeRecipe(Recipe recipe) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        Iterator<String> ingredientIter = recipe.getIngredientIterator();
        Iterator<String> instructionIter = recipe.getInstructionIterator();
        String ingredient, instruction;

        strBuilder.append(recipe.getId().toString()).append("::");
        strBuilder.append(recipe.getAccountId().toString()).append("::");
        strBuilder.append(recipe.getTitle()).append("::");
        strBuilder.append(recipe.getMealTag()).append("::");

        // ingredients
        while (ingredientIter.hasNext()) {
            ingredient = ingredientIter.next();
            strBuilder.append(ingredient);
            if (ingredientIter.hasNext()) {
                strBuilder.append(";;");
            }
        }
        strBuilder.append("::");

        // instructions
        while (instructionIter.hasNext()) {
            instruction = instructionIter.next();
            strBuilder.append(instruction);
            if (instructionIter.hasNext()) {
                strBuilder.append(";;");
            }
        }
        strBuilder.append("::");
        // image
        strBuilder.append(recipe.getImage());

        strBuilder.append("\n");
        writer.write(strBuilder.toString());
    }

    public void writeRecipeDb(IRecipeDb recipeDb) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        // use "::" as a delimiter for the csv files
        strBuilder.append("sep=::").append("\n");
        // add labels for the columns of the csv file
        strBuilder.append("ID::Account::Title::Tag::Ingredients::Instructions::Image").append("\n");
        writer.write(strBuilder.toString());

        for (Recipe recipe : recipeDb.getList()) {
            writeRecipe(recipe);
        }
    }

    public void writeRecipeList(List<Recipe> recipes) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        // use "::" as a delimiter for the csv files
        strBuilder.append("sep=::").append("\n");
        // add labels for the columns of the csv file
        strBuilder.append("ID::Account::Title::Tag::Ingredients::Instructions::Image").append("\n");
        writer.write(strBuilder.toString());

        for (Recipe recipe : recipes) {
            writeRecipe(recipe);
        }
    }
}
