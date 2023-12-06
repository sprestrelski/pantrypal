package code;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import code.client.Model.AppConfig;
import code.client.Model.ServerConnection;
import code.server.BaseServer;
import code.server.mocking.MockServer;

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
    void testServerOffline() throws IOException {
        BaseServer server = new MockServer("localhost", AppConfig.SERVER_PORT);
        ServerConnection connection = new ServerConnection(server);
        assertFalse(connection.isOnline());
        assertEquals("Server is offline", outData.toString());
    }

    @Test
    void testServerOnline() throws IOException {
        BaseServer server = new MockServer("localhost", AppConfig.SERVER_PORT);
        ServerConnection connection = new ServerConnection(server);
        server.start();
        assertTrue(connection.isOnline());
        assertTrue(outData.toString().contains("Server is online"));
        server.stop();
    }
}
