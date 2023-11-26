package code.client.Model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;

public class MockWhisperService extends VoiceToText {
    public MockWhisperService(IHttpConnection connection) {
        super(connection);
    }

    @Override
    public String processAudio() throws IOException, URISyntaxException {
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
}
