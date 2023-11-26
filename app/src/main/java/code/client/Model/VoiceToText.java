package code.client.Model;

import java.io.*;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class VoiceToText {
    protected final IHttpConnection connection;

    public VoiceToText(IHttpConnection connection) {
        this.connection = connection;
    }

    public abstract String processAudio() throws IOException, URISyntaxException;

    // Helper method to handle a successful response
    protected String handleSuccessResponse() throws IOException, JSONException {
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
    protected String handleErrorResponse() throws IOException, JSONException {
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
