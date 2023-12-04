package code.server;

import java.io.IOException;
import java.net.URISyntaxException;

import code.client.Model.IHttpConnection;
import code.client.Model.MockHttpConnection;

public class MockWhisperRequestHandler extends VoiceToText {
    public MockWhisperRequestHandler() {
        super(new MockHttpConnection(200));
    }

    public MockWhisperRequestHandler(IHttpConnection connection) {
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
