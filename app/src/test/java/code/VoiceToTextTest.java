package code;

import java.io.*;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import code.client.Model.IHttpConnection;
import code.client.Model.MockHttpConnection;
import code.client.Model.MockWhisperService;
import code.client.Model.VoiceToText;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VoiceToTextTest {
    /*
     * Integration Test
     */
    @Test
    void testSuccessfulProcessAudio() throws IOException, URISyntaxException {
        IHttpConnection connection = new MockHttpConnection(
                200,
                new ByteArrayInputStream("{\"text\":\"Breakfast.\"}".getBytes()),
                new ByteArrayOutputStream());

        VoiceToText voiceToText = new MockWhisperService(connection);
        String response = voiceToText.processAudio();
        assertEquals("Breakfast.", response);

        connection = new MockHttpConnection(
                200,
                new ByteArrayInputStream("{\"text\":\"Chicken, cheese.\"}".getBytes()),
                null);

        voiceToText = new MockWhisperService(connection);
        response = voiceToText.processAudio();
        assertEquals("Chicken, cheese.", response);

    }

    /*
     * Unit test
     */
    @Test
    void testFailedProcessAudio() throws IOException, URISyntaxException {
        IHttpConnection connection = new MockHttpConnection(
                404,
                new ByteArrayInputStream("Error text".getBytes()),
                null);

        VoiceToText voiceToText = new MockWhisperService(connection);
        String response = voiceToText.processAudio();
        assertEquals("Error text", response);
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