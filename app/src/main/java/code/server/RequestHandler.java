package code.server;

import com.sun.net.httpserver.*;

import code.client.Model.IRecipeDb;
import code.client.Model.Recipe;
import code.client.Model.RecipeReader;
import code.client.Model.RecipeWriter;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.*;
import java.net.*;
import java.util.*;

public class RequestHandler implements HttpHandler {
    private static final String CSV_FILE = "recipes.csv";
    private IRecipeDb recipeDb;

    public RequestHandler() {
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        loadRecipes();
        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    /*
     * Expects UUID
     */
    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        if (query != null) {
            UUID uuid = UUID.fromString(query.substring(query.indexOf("=") + 1));
            Recipe recipe = recipeDb.find(uuid);
            if (recipe != null) {
                response = recipe.getHttpString();
                System.out.println("Queried for " + uuid + " and found " + recipe.getTitle());
            } else {
                response = "Recipe not found.";
            }
        }

        return response;
    }

    private Recipe buildRecipe(String postData) throws IOException {
        Reader reader = new StringReader(postData);
        // System.out.println("building recipe from: " + postData);
        RecipeReader recipeReader = new RecipeReader(reader);
        Recipe recipe = recipeReader.readRecipe();
        return recipe;
    }

    /*
     * Expects recipe as recipe| ingredients | instructions
     */
    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        Recipe recipe = buildRecipe(postData);
        recipeDb.add(recipe);
        String response = "Posted entry " + recipe.getTitle();
        System.out.println(response);
        scanner.close();
        return response;
    }

    /*
     * Expects recipe as recipe| ingredients | instructions
     */
    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        Recipe recipe = buildRecipe(postData);

        Recipe previous = recipeDb.find(recipe.getId());
        if (previous != null) {
            recipeDb.remove(previous);
        }

        recipeDb.add(recipe);
        String response = "Added entry " + recipe.getTitle();
        System.out.println(response);
        scanner.close();

        return response;
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Invalid DELETE request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        if (query != null) {
            UUID uuid = UUID.fromString(query.substring(query.indexOf("=") + 1));
            Recipe recipe = recipeDb.find(uuid);
            if (recipe != null) {
                recipeDb.remove(recipe);
                response = recipe.getHttpString();
                System.out.println("Queried for " + uuid + " and found " + recipe.getTitle());
            } else {
                System.out.println("Recipe not found.");
            }
        }

        return response;
    }

    // /*
    // * Save recipes to a file called "recipes.csv"
    // */
    // public void saveRecipes() {
    // try {
    // Writer writer = new BufferedWriter(new FileWriter(CSV_FILE));
    // RecipeWriter recipeWriter = new RecipeWriter(writer);
    // recipeWriter.writeRecipeDb(recipeDb);
    // writer.close();
    // } catch (IOException e) {
    // System.out.println("Recipes could not be saved.");
    // }
    // }

    /*
     * Load recipes from a file called "recipes.csv" to RecipeDb
     */
    public void loadRecipes() {
        try {
            Reader reader = new FileReader(CSV_FILE);
            RecipeReader recipeReader = new RecipeReader(reader);
            recipeDb = recipeReader.readRecipeDb();
            System.out.println("Recipes loaded");
        } catch (IOException e) {
            System.out.println("Recipes could not be loaded.");
        }
    }

}