package code;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import code.client.Model.AppConfig;
import code.client.Model.Model;
import code.server.BaseServer;
import code.server.IHttpConnection;
import code.server.mocking.MockHttpConnection;
import code.server.mocking.MockServer;
import java.net.ConnectException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VoiceToTextTest {
    BaseServer server = new MockServer("localhost", AppConfig.SERVER_PORT);
    Model model = new Model();

    /*
     * Integration Test for processing both audios
     */
    @Test
    void testSuccessfulProcessAudio() throws IOException, URISyntaxException {
        server.start();
        String response = model.performWhisperRequest("GET", "mealType");
        assertEquals("Breakfast", response);

        response = model.performWhisperRequest("GET", "ingredients");
        assertEquals("Chicken, eggs.", response);
        server.stop();
    }

    @Test
    void testUnsuccessfulProcessAudio() throws MalformedURLException, IOException {
        server.start();
        try {
            String response = model.performWhisperRequest("GET", "error");
            assertEquals("Error", response);
        } catch (ConnectException e) {
            assert (false);
        }
        server.stop();
    }

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