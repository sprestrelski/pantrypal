package code.server;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.bson.types.ObjectId;

import java.util.Iterator;
import java.util.List;

import code.client.Model.*;

import com.sun.net.httpserver.*;

public class ShareRequestHandler implements HttpHandler {

    private AccountMongoDB accountMongoDB;
    private IRecipeDb recipeMongoDb;

    public ShareRequestHandler(AccountMongoDB accountMongoDB, IRecipeDb recipeMongoDb) {
        this.accountMongoDB = accountMongoDB;
        this.recipeMongoDb = recipeMongoDb;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        String query = uri.toString();
        int usernameStart = query.indexOf(AppConfig.SHARE_PATH);
        String username = query.substring(usernameStart + AppConfig.SHARE_PATH.length());
        String recipeID = username.substring(username.indexOf("/") + 1);
        username = username.substring(0,username.indexOf("/"));

        // Format: localhost:8100/recipes/username/recipeID

        // System.out.println("\n" + uri.toString());
        // System.out.println(username);
        // System.out.println(recipeID);
        response = getSharedRecipe(username,recipeID);
        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    private String getSharedRecipe(String username, String recipeID) {
        Account checkUser = accountMongoDB.findByUsername(username);
        
        if(checkUser == null) {
            return nonExistentRecipe();
        }
        //System.out.println("Found user" + checkUser.getUsername());
        String accountID = checkUser.getId();
        List<Recipe> accRecipes = recipeMongoDb.getList(accountID);
        Recipe foundRecipe = null;
        for(int i = 0; i < accRecipes.size(); i++) {
            if(accRecipes.get(i).getId().equals(recipeID)) {
                foundRecipe = accRecipes.get(i);
            }
        }
        
        if(foundRecipe == null) {
            return nonExistentRecipe();
        }
        System.out.println("Found recipe" + foundRecipe.getTitle());
        return formatRecipe(foundRecipe);
    }

    private String nonExistentRecipe() {
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

    private String getMockedRecipe() {
        String title = "Fried Chicken and Egg Fried Rice";
        String mealType = "Breakfast";
        String ingredients = "2 chicken breasts, diced;;2 large eggs;;2 cups cooked rice;;2 tablespoons vegetable oil";
        String[] ingr = ingredients.split(";;");
        String instructions = "1. Heat the vegetable oil in a large pan over medium-high heat.";
        String[] instr = instructions.split(";;");
        //return formatRecipe(title, ingr, instr);
        return title;
    }

    private String formatRecipe(Recipe recipe) {
        Iterator<String> ingr = recipe.getIngredientIterator();
        Iterator<String> instr = recipe.getInstructionIterator();
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder
        .append("<html>")
        .append("<title>" + recipe.getTitle() + "</title>")
        .append("<body>")

        .append("<h1>" + recipe.getTitle() + "</h2>")
        .append("<h2>")
            .append("Ingredients: ")
        .append("</h2>")
            .append("<p>")
                .append("<ul>");
                    while(ingr.hasNext()) {
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
                    while(instr.hasNext()) {
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
