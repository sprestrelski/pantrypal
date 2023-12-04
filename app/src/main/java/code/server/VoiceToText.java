package code.server;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;
import code.client.Model.IHttpConnection;

public abstract class VoiceToText {
    protected final IHttpConnection connection;

    public VoiceToText(IHttpConnection connection) {
        this.connection = connection;
    }

    public String processAudio(String type) throws IOException, URISyntaxException {
        String response = handleResponse();
        if (type.equals("mealtype")) {
            response = response.toUpperCase();
            if (response.contains("BREAKFAST")) {
                response = "Breakfast";
            } else if (response.contains("LUNCH")) {
                response = "Lunch";
            } else if (response.contains("DINNER")) {
                response = "Dinner";
            } else {
                response = null;
            }
        }
        return response;
    }

    private String handleResponse() throws IOException {
        // Get response code
        int responseCode = connection.getResponseCode();
        String response;

        // Check response code and handle response accordingly
        if (responseCode == HttpURLConnection.HTTP_OK) {
            response = handleSuccessResponse();
        } else {
            response = handleErrorResponse();
        }

        return response;
    }

    // Helper method to handle a successful response
    private String handleSuccessResponse() throws IOException, JSONException {
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = responseReader.readLine()) != null) {
            response.append(inputLine);
        }

        responseReader.close();
        JSONObject responseJson = new JSONObject(response.toString());
        String generatedText = responseJson.getString("text");
        return generatedText;
    }

    // Helper method to handle an error response
    private String handleErrorResponse() throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();

        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }

        errorReader.close();
        String errorResult = errorResponse.toString();
        return errorResult;
    }
}
