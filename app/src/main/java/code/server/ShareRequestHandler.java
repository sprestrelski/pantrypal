package code.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import code.AppConfig;
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
        URI uri = httpExchange.getRequestURI();
        String query = uri.toString();
        int usernameStart = query.indexOf(AppConfig.SHARE_PATH);
        String username = query.substring(usernameStart + AppConfig.SHARE_PATH.length());
        String recipeID = username.substring(username.indexOf("/") + 1);
        username = username.substring(0, username.indexOf("/"));

        // Format: localhost:8100/recipes/username/recipeID

        System.out.println("\n" + uri.toString());
        System.out.println(username);
        System.out.println(recipeID);
        response = ShareRecipe.getSharedRecipe(accountMongoDB, recipeMongoDb, username, recipeID);
        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

}
