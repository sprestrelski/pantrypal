package code.client.Model;

import java.io.IOException;
import java.net.URISyntaxException;

public class MockWhisperService extends VoiceToText {
    public MockWhisperService() {
        super(new MockHttpConnection(200));
    }

    public MockWhisperService(IHttpConnection connection) {
        super(connection);
    }

    public String processAudio(String type) throws IOException, URISyntaxException {
        if (type.equals("mealtype")) {
            // processed correctly
            return "Breakfast";
        } else if (type.equals("ingredients")) {
            return "Chicken, eggs.";
        } else {
            return "Error text";
        }
    }
}
