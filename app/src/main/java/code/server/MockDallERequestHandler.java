package code.server;

import code.client.Model.AppConfig;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;

import java.io.IOException;
import java.nio.file.Files;

import java.util.Base64;

public class MockDallERequestHandler extends RecipeToImage implements HttpHandler {
    private boolean error = false;

    @Override
    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request received";
        String method = httpExchange.getRequestMethod();
        System.out.println("Method is " + method);

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else {
                throw new Exception("Not valid request method.");
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

    private String handleGet(HttpExchange httpExchange) throws IOException, InterruptedException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        if (query != null && !error) {
            String recipeTitle = query.substring(query.indexOf("=") + 1);
            response = getResponse(recipeTitle);
        }
        return response;
    }

    private String getResponse(String recipeText) throws IOException, InterruptedException {
        File file = new File(AppConfig.RECIPE_IMG_FILE);
        // try to give default image
        try {
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception fileError) {
            fileError.printStackTrace();
            return "RnJpZWQgUmljZSBJbWFnZSA6KQ==";
        }
    }

}
