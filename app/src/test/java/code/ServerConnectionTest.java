package code;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import code.client.Model.AppConfig;
import code.client.View.ServerConnection;
import code.server.MockServer;

import java.io.IOException;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import static org.junit.jupiter.api.Assertions.*;

public class ServerConnectionTest {
    private final ByteArrayOutputStream outData = new ByteArrayOutputStream();
    private final PrintStream outStream = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outData));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(outStream);
    }

    @Test
    void testServerOffline() {
        MockServer server = new MockServer(AppConfig.SERVER_HOST, AppConfig.SERVER_PORT);
        ServerConnection connection = new ServerConnection(server);
        assertFalse(connection.isOnline());
        assertEquals("Server is offline", outData.toString());
    }

    @Test
    void testServerOnline() throws IOException {
        // Googles public DNS : should be true
        MockServer server = new MockServer("8.8.8.8", 443);
        ServerConnection connection = new ServerConnection(server);
        server.start();
        assertTrue(connection.isOnline());
        assertEquals("Server is online", outData.toString());
    }
}
