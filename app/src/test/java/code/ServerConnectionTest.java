package code;

import org.junit.jupiter.api.Test;

import code.client.Model.AppConfig;
import code.client.View.ServerConnection;
import code.server.MockServer;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class ServerConnectionTest {
    @Test
    void testServerOffline() {
        MockServer server = new MockServer(AppConfig.SERVER_HOST, AppConfig.SERVER_PORT);
        ServerConnection connection = new ServerConnection(server);
        assertFalse(connection.isOnline());
    }

    @Test
    void testServerOnline() throws IOException {
        // Googles public DNS : should be true
        MockServer server = new MockServer("8.8.8.8", 443);
        ServerConnection connection = new ServerConnection(server);
        server.start();
        assertTrue(connection.isOnline());
    }
}
