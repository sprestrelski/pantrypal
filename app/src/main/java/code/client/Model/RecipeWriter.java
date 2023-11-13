package code.client.Model;

import java.io.Writer;
import java.io.IOException;
import java.util.Iterator;

public class RecipeWriter {
    private final Writer writer;

    public RecipeWriter(Writer writer) {
        this.writer = writer;
    }

    public void writeRecipe(Recipe recipe) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        Iterator<String> ingredientIter = recipe.getIngredientIterator();
        Iterator<String> instructionIter = recipe.getInstructionIterator();
        String ingredient, instruction;

        strBuilder.append(recipe.getTitle()).append("| ");

        while (ingredientIter.hasNext()) {
            ingredient = ingredientIter.next();
            if (ingredientIter.hasNext()) {
                strBuilder.append(ingredient).append(";;");
            }
            else {
                strBuilder.append(ingredient);
            }
        }

        strBuilder.append("| ");

        while (instructionIter.hasNext()) {
            instruction = instructionIter.next();
            if (instructionIter.hasNext()) {
                strBuilder.append(instruction).append(";;");
            }
            else {
                strBuilder.append(instruction);
            }
        }

        strBuilder.append("\n");
        writer.write(strBuilder.toString());
    }

    public void writeRecipeDb(IRecipeDb recipeDb) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        // use "|" as a delimeter for the csv files
        strBuilder.append("sep=|").append("\n");
        // add labels for the columns of the csv file
        strBuilder.append("Recipe Name| Ingredients| Instructions").append("\n");
        writer.write(strBuilder.toString());
        for (Recipe recipe : recipeDb) {
            writeRecipe(recipe);
        }
    }
}
