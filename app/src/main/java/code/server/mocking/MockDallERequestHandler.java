package code.server.mocking;

import code.AppConfig;
import code.server.RecipeToImage;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.nio.file.Files;

import java.util.Base64;

public class MockDallERequestHandler extends RecipeToImage implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request received";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        try {
            String recipeTitle = query.substring(query.indexOf("=") + 1);
            response = getResponse(recipeTitle);
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

    private String getResponse(String recipeText) throws IOException, InterruptedException {
        File file = new File(AppConfig.RECIPE_IMG_FILE);
        // try to give default image
        try {
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception fileError) {
            fileError.printStackTrace();
            return "RnJpZWQgUmljZSBJbWFnZQ==";
        }
    }

}
