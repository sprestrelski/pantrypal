package code;

import java.io.*;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import code.client.Model.CustomHttpConnection;
import code.client.Model.WhisperHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VoiceRetrievalTest {

    /*
     * Integration Tests
     */
    @Test
    void testSuccessfulProcessAudio() throws IOException, URISyntaxException {
        CustomHttpConnection connection = new MockHttpConnection(200,
                new ByteArrayInputStream("{\"text\":\"Breakfast.\"}".getBytes()), new ByteArrayOutputStream());
        WhisperHandler audioProcessor = new WhisperHandler("API_ENDPOINT", "TOKEN", "MODEL", connection);
        String response = audioProcessor.processAudio();
        assertEquals("Breakfast.", response);

        audioProcessor.setHttpConnection(new MockHttpConnection(200,
                new ByteArrayInputStream("{\"text\":\"Chicken, cheese.\"}".getBytes()), null));
        response = audioProcessor.processAudio();
        assertEquals("Chicken, cheese.", response);

    }

    @Test
    void testFailedProcessAudio() throws IOException, URISyntaxException {
        CustomHttpConnection connection = new MockHttpConnection(404,
                new ByteArrayInputStream("Error text".getBytes()),
                null);
        WhisperHandler audioProcessor = new WhisperHandler("API_ENDPOINT", "TOKEN", "MODEL", connection);
        String response = audioProcessor.processAudio();
        assertEquals("Error text", response);
    }

    /*
     * Unit tests
     */
    @Test
    void testMockHttpCreation() throws IOException {
        CustomHttpConnection connection = new MockHttpConnection(200, new ByteArrayInputStream("hello".getBytes()),
                null);
        int responseCode = connection.getResponseCode();
        assertEquals(responseCode, 200);
        assertEquals(connection.getInputStream(), connection.getErrorStream());

    }

}
