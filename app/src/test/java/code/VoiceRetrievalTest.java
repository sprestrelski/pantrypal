package code;

import java.io.*;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import code.client.Model.IHttpConnection;
import code.client.Model.WhisperService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VoiceRetrievalTest {

    /*
     * Integration Tests
     */
    @Test
    void testSuccessfulProcessAudio() throws IOException, URISyntaxException {
        IHttpConnection connection = new MockHttpConnection(
                200,
                new ByteArrayInputStream("{\"text\":\"Breakfast.\"}".getBytes()),
                new ByteArrayOutputStream());

        WhisperService audioProcessor = new WhisperService(connection);
        String response = audioProcessor.processAudio();
        assertEquals("Breakfast.", response);

        audioProcessor.setHttpConnection(
                new MockHttpConnection(
                        200,
                        new ByteArrayInputStream("{\"text\":\"Chicken, cheese.\"}".getBytes()),
                        null));

        response = audioProcessor.processAudio();
        assertEquals("Chicken, cheese.", response);

    }

    @Test
    void testFailedProcessAudio() throws IOException, URISyntaxException {
        IHttpConnection connection = new MockHttpConnection(
                404,
                new ByteArrayInputStream("Error text".getBytes()),
                null);

        WhisperService audioProcessor = new WhisperService(connection);
        String response = audioProcessor.processAudio();
        assertEquals("Error text", response);
    }

    /*
     * Unit tests
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