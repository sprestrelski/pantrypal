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

        return String.format("""
                <html>
                    <head>
                        <title>%s</title>
                        <style>
                        body {
                            font-family: 'Comic Sans MS', cursive;
                            background-color: #f4f4f4;
                            color: #333;
                            margin: 0;
                            padding: 0;
                            display: flex;
                            flex-direction: column;
                            align-items: center;
                        }
                        h1, h2 {
                            color: #007bff;
                        }
                        img {
                            width: 100%;
                            max-width: 600px;
                            height: auto;
                            margin-top: 10px;
                        }
                        ul {
                            list-style-type: none;
                            padding: 0;
                            margin: 0;
                        }
                        li {
                            background-color: #fff;
                            border: 1px solid #ddd;
                            margin: 5px 0;
                            padding: 10px;
                            border-radius: 5px;
                            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                        }
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
                recipe.getTitle(),
                recipe.getTitle(),
                recipe.getImage(),
                buildList(ingr),
                buildList(instr));
    }

    private static String buildList(Iterator<String> iterator) {
        StringBuilder listBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            listBuilder.append(String.format("<li>%s</li>", iterator.next()));
        }
        return listBuilder.toString();
    }

}
