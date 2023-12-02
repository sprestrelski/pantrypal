package code.server;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import com.sun.net.httpserver.*;

public class RecipeSharingHandler implements HttpHandler {

    private AccountMongoDB accountMongoDB;

    public RecipeSharingHandler(AccountMongoDB accountMongoDB) {
        this.accountMongoDB = accountMongoDB;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String route = "/recipes/";
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        String query = uri.toString();
        int usernameStart = query.indexOf(route);

        String username = query.substring( usernameStart + route.length());
        String recipeID = username.substring(username.indexOf("/") + 1);
        username = username.substring(0,username.indexOf("/"));

         
        // System.out.println("\n" + uri.toString());
        // System.out.println(username);
        // System.out.println(recipeID);
        response = getMockedRecipe();//getSharedRecipe(username,recipeID);
        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    private String getSharedRecipe(String username, String recipeID) {
        Account checkUser = accountMongoDB.find(username);
        if(checkUser == null) return nonExistentRecipe();
        
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder
        .append("<style>parent{display: flex;}")
        .append("child{justify-content: center;\nalign-items: center;}")
        .append("</style>")
        .append("<div class=\"parent\"> ")
        .append("<iframe class=\"child\" width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/dQw4w9WgXcQ?autoplay=1\" title=\"YouTube video player\" frameborder=\"0\"allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen ></iframe>")
        .append("</div> ");

        return htmlBuilder.toString();
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

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder
        .append("<html>")
        .append("<title>" + title + "</title>")
        .append("<body>")

        .append("<h1>" + title + "</h2>")
        .append("<h2>")
            .append("Ingredients: ")
        .append("</h2>")
            .append("<p>")
                .append("<ul>");
                    for(int i = 0; i < ingr.length; i++) {
                        htmlBuilder.append("<li>" + ingr[i] + "</li>");
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
                    for(int i = 0; i < instr.length; i++) {
                        htmlBuilder.append("    " + instr[i]);
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
