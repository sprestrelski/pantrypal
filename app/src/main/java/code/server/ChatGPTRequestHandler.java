package code.server;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class ChatGPTRequestHandler implements HttpHandler {
    private final TextToRecipe textToRecipe;

    public ChatGPTRequestHandler(TextToRecipe textToRecipe) {
        this.textToRecipe = textToRecipe;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request received";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        try {
            String value = query.substring(query.indexOf("=") + 1);
            String[] typeIngredients = value.split("::");
            String mealType = typeIngredients[0];
            String ingredients = typeIngredients[1];
            response = textToRecipe.getResponse(mealType, ingredients);
        } catch (IndexOutOfBoundsException e) {
            response = "Provide valid meal type or ingredients";
            e.printStackTrace();
        } catch (InterruptedException | URISyntaxException e) {
            response = "An error occurred.";
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }
}