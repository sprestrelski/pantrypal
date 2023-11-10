package code.client.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONException;

public class WhisperHandler {
    private String API_ENDPOINT;
    private String TOKEN;
    private String MODEL;

    public WhisperHandler(String API_ENDPOINT, String TOKEN, String MODEL) {
        this.API_ENDPOINT = API_ENDPOINT;
        this.TOKEN = TOKEN;
        this.MODEL = MODEL;
    }

    // https://stackoverflow.com/questions/25334139/how-to-mock-a-url-connection
    public String processAudio() throws IOException, URISyntaxException {
        // Create file object from file path
        File file = new File("recording.wav");
        HttpURLConnection connection = sendHttpRequest(file);

        // Get response code
        int responseCode = connection.getResponseCode();
        String response;

        // Check response code and handle response accordingly
        if (responseCode == HttpURLConnection.HTTP_OK) {
            response = handleSuccessResponse(connection);
        } else {
            response = handleErrorResponse(connection);
        }

        // Disconnect connection
        connection.disconnect();

        return response;
    }

    private HttpURLConnection sendHttpRequest(File file) throws IOException, URISyntaxException {
        URL url = new URI(API_ENDPOINT).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set up request headers
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

        // Set up output stream to write request body
        OutputStream outputStream = connection.getOutputStream();

        // Write model parameter to request body
        writeParameterToOutputStream(outputStream, "model", MODEL, boundary);

        // Write file parameter to request body
        writeFileToOutputStream(outputStream, file, boundary);

        // Write closing boundary to request body
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

        // Flush and close output stream
        outputStream.flush();
        outputStream.close();

        return connection;
    }

    // Helper method to write a parameter to the output stream in multipart form
    // data format
    private static void writeParameterToOutputStream(
            OutputStream outputStream,
            String parameterName,
            String parameterValue,
            String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
                ("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n").getBytes());
        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    // Helper method to write a file to the output stream in multipart form data
    // format
    private static void writeFileToOutputStream(
            OutputStream outputStream,
            File file,
            String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
                ("Content-Disposition: form-data; name=\"file\"; filename=\"" +
                        file.getName() +
                        "\"\r\n").getBytes());
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }

    // Helper method to handle a successful response
    private static String handleSuccessResponse(HttpURLConnection connection)
            throws IOException, JSONException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject responseJson = new JSONObject(response.toString());

        String generatedText = responseJson.getString("text");
        System.out.println("Transcription Result: " + generatedText);
        return generatedText;

    }

    // Helper method to handle an error response
    private static String handleErrorResponse(HttpURLConnection connection)
            throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(connection.getErrorStream()));
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
        System.out.println("Error Result: " + errorResult);
        return errorResult;
    }

}
