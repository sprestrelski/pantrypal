package code;

import java.io.*;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import code.client.Model.AppConfig;
import code.client.Model.Model;
import code.server.BaseServer;
import code.server.IHttpConnection;
import code.server.MockHttpConnection;
import code.server.MockServer;
import code.server.MockWhisperRequestHandler;
import code.server.VoiceToText;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VoiceToTextTest {
    BaseServer server = new MockServer(AppConfig.SERVER_HOST, AppConfig.SERVER_PORT);
    Model model = new Model();

    /*
     * Integration Test for processing both audios
     */
    @Test
    void testSuccessfulProcessAudio() throws IOException, URISyntaxException {
        VoiceToText voiceToText = new MockWhisperRequestHandler();
        String response = voiceToText.processAudio("mealType");
        assertEquals("Breakfast", response);

        response = voiceToText.processAudio("ingredients");
        assertEquals("Chicken, eggs.", response);
    }

    /*
     * Unit test
     */
    @Test
    void testMockHttpCreation() throws IOException {
        IHttpConnection connection = new MockHttpConnection(
                200,
                new ByteArrayInputStream("hello".getBytes()),
                null);

        int responseCode = connection.getResponseCode();
        assertEquals(responseCode, 200);
        assertEquals(connection.getInputStream(), connection.getErrorStream());
    }

}