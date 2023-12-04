package code.server;

import java.util.Iterator;
import java.util.List;

public class ShareRecipe {
    public static String getSharedRecipe(AccountMongoDB accountMongoDB, IRecipeDb recipeMongoDb, String username,
            String recipeID) {
        Account checkUser = accountMongoDB.findByUsername(username);

        if (checkUser == null) {
            return nonExistentRecipe();
        }
        // System.out.println("Found user" + checkUser.getUsername());
        String accountID = checkUser.getId();
        List<Recipe> accRecipes = recipeMongoDb.getList(accountID);
        Recipe foundRecipe = null;
        for (int i = 0; i < accRecipes.size(); i++) {
            if (accRecipes.get(i).getId().equals(recipeID)) {
                foundRecipe = accRecipes.get(i);
            }
        }

        if (foundRecipe == null) {
            return nonExistentRecipe();
        }
        System.out.println("Found recipe" + foundRecipe.getTitle());
        return formatRecipe(foundRecipe);
    }

    private static String nonExistentRecipe() {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder
                .append("<html>")
                .append("<body>")
                .append("<h1>")
                .append("The provided link is not associated to a recipe.")
                .append("</h1>")
                .append("</body>")
                .append("</html>");
        // encode HTML content
        return htmlBuilder.toString();
    }

    private static String getMockedRecipe() {
        String title = "Fried Chicken and Egg Fried Rice";
        String mealType = "Breakfast";
        String ingredients = "2 chicken breasts, diced;;2 large eggs;;2 cups cooked rice;;2 tablespoons vegetable oil";
        String[] ingr = ingredients.split(";;");
        String instructions = "1. Heat the vegetable oil in a large pan over medium-high heat.";
        String[] instr = instructions.split(";;");
        // return formatRecipe(title, ingr, instr);
        return title;
    }

    private static String formatRecipe(Recipe recipe) {
        Iterator<String> ingr = recipe.getIngredientIterator();
        Iterator<String> instr = recipe.getInstructionIterator();
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder
                .append("<html>")
                .append("<title>" + recipe.getTitle() + "</title>")
                .append("<body>")
                .append("<h1>" + recipe.getTitle() + "</h2>")
                .append("<img src=\"data:image/png;base64,")
                .append(recipe.getImage())
                .append("\" width=\"256\"alt=\"Recipe Image\">")
                .append("<h2>")
                .append("Ingredients: ")
                .append("</h2>")
                .append("<p>")
                .append("<ul>");
        while (ingr.hasNext()) {
            htmlBuilder.append("<li>" + ingr.next() + "</li>");
        }
        htmlBuilder
                .append("</ul>")
                .append("</p>")
                .append("</h2>")
                .append("<h1>")
                .append("Instructions: ")
                .append("</h2>")
                .append("<p>")
                .append("<ul>");
        while (instr.hasNext()) {
            htmlBuilder.append("<li>" + instr.next() + "</li>");
        }
        htmlBuilder
                .append("</ul>")
                .append("</p>")
                .append("</h2>");

        htmlBuilder
                .append("</body>")
                .append("</html>");

        // encode HTML content
        return htmlBuilder.toString();
    }

}
