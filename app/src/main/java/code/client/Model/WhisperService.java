package code.client.Model;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;

public class WhisperService extends VoiceToText {
    public static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    public static final String MODEL = "whisper-1";

    public WhisperService() throws URISyntaxException, IOException {
        super(new AppHttpConnection(API_ENDPOINT));
    }

    // https://stackoverflow.com/questions/25334139/how-to-mock-a-url-connection
    public String processAudio() throws IOException, URISyntaxException {
        // Send HTTP request
        sendHttpRequest();
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

    private IHttpConnection sendHttpRequest() throws IOException, URISyntaxException {
        // Set up request headers
        File file = new File(AppConfig.AUDIO_FILE);
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("Authorization", "Bearer " + AppConfig.API_KEY);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
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
        outputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" +
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
}
