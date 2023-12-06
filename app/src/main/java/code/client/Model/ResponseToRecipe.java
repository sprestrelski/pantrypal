package code.client.Model;

import code.server.Recipe;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ResponseToRecipe {
    public Recipe getRecipe(String responseText) {
        // Split the tokens into lines
        String[] tokenArr = responseText.split("\n");
        List<String> tokenList = new ArrayList<>(Arrays.asList(tokenArr));
        int i;

        // Remove empty tokens
        for (i = 0; i < tokenList.size();) {
            if (tokenList.get(i).isBlank()) {
                tokenList.remove(i);
            } else {
                tokenList.set(i, tokenList.get(i).trim());
                ++i;
            }
        }

        // Parse recipe's title and create a new recipe
        String title = tokenList.get(0);
        Recipe recipe = new Recipe(title, null);
        // Parse recipe's ingredients
        String ingredient;
        boolean parse = false;

        for (i = 0; !tokenList.get(i).contains("Instructions"); ++i) {
            ingredient = tokenList.get(i);
            if (ingredient.contains("Ingredients")) {
                parse = true;
            } else if (parse) {
                ingredient = removeDashFromIngredient(tokenList.get(i));
                recipe.addIngredient(ingredient);
            }
        }

        // Parse recipe's instructions
        String instruction;
        for (i += 1; i < tokenList.size(); ++i) {
            instruction = removeNumberFromInstruction(tokenList.get(i));
            recipe.addInstruction(instruction);
        }

        return recipe;
    }

    private String removeDashFromIngredient(String ingredient) {
        if (ingredient.charAt(0) == ('-')) {
            return ingredient.substring(1).trim();
        }
        return ingredient.substring(2);
    }

    private String removeNumberFromInstruction(String instruction) {
        StringBuilder strBuilder = new StringBuilder();
        int i;

        // Ignore characters until '.'
        for (i = 0; i < instruction.length() && (instruction.charAt(i) != '.'); ++i)
            ;

        // Ignore '.' and ' ' after
        // Get all characters until end of string
        for (i += 2; i < instruction.length(); ++i) {
            strBuilder.append(instruction.charAt(i));
        }

        return strBuilder.toString();
    }
}
