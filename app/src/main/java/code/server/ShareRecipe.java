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

    // private static String getMockedRecipe() {
    // String title = "Fried Chicken and Egg Fried Rice";
    // String mealType = "Breakfast";
    // String ingredients = "2 chicken breasts, diced;;2 large eggs;;2 cups cooked
    // rice;;2 tablespoons vegetable oil";
    // String[] ingr = ingredients.split(";;");
    // String instructions = "1. Heat the vegetable oil in a large pan over
    // medium-high heat.";
    // String[] instr = instructions.split(";;");
    // // return formatRecipe(title, ingr, instr);
    // return title;
    // }

    private static String formatRecipe(Recipe recipe) {
        String title = recipe.getTitle() != null ? recipe.getTitle() : "Untitled Recipe";
        String image = recipe.getImage() != null ? recipe.getImage() : ""; // Provide a default image or an empty string
        String ingr = buildList(recipe.getIngredientIterator());
        String instr = buildList(recipe.getInstructionIterator());

        return String.format("""
                <html>
                    <head>
                        <title>%s</title>
                        <style>
                        body { font-family: 'Comic Sans MS', cursive; }
                        h1, h2 { font-family: 'Comic Sans MS', cursive; color: #333; }
                        ul { list-style-type: square; }
                    </style>
                    </head>
                    <body>
                        <h1>%s</h1>
                        <img src="data:image/png;base64,%s" width="256" alt="Recipe Image">
                        <h2>Ingredients:</h2>
                        <p><ul>
                            %s
                        </ul></p>
                        <h2>Instructions:</h2>
                        <p><ul>
                            %s
                        </ul></p>
                    </body>
                </html>
                """,
                title,
                title,
                image,
                ingr,
                instr);
    }

    private static String buildList(Iterator<String> iterator) {
        StringBuilder listBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            listBuilder.append(String.format("<li>%s</li>", iterator.next()));
        }
        return listBuilder.toString();
    }

}
