package code;

import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.*;

import code.client.Model.ServerCheck;
import code.server.MyServer;

import java.io.IOException;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServerAvailabilityTest {
    // moc
    static class TestHttpServer extends HttpServer {

        boolean createContextCalled = false;
        boolean setExecutorCalled = false;
        boolean startCalled = false;

        protected TestHttpServer() throws IOException {
            super();
        }

        @Override
        public HttpContext createContext(String s, HttpHandler httpHandler) {
            createContextCalled = true;
            return null;
        }

        @Override
        public void setExecutor(Executor executor) {
            setExecutorCalled = true;
        }

        public Executor getExecutor() {
            return null;
        }

        @Override
        public void start() {
            startCalled = true;
        }

        @Override
        public void stop(int i) {
        }

        public HttpContext createContext(String s) {
            createContextCalled = true;
            return null;
        }

        public void removeContext(HttpContext h) {
        }

        public void removeContext(String s) {
        }

        public void bind(InetSocketAddress i, int j) {
        }

        public InetSocketAddress getAddress() {
            return new InetSocketAddress("hello", 8);
        }
    }

    private static class TestSocket extends Socket {
        private boolean connected = true;

        @Override
        public boolean isConnected() {
            return connected;
        }

        @Override
        public boolean isClosed() {
            return !connected;
        }

        public void setConnected(boolean connected) {
            this.connected = connected;
        }
    }

    @Test
    void testServerStart() throws IOException {
        // create test server
        TestHttpServer testHttpServer = new TestHttpServer();
        MyServer myServer = new MyServer(testHttpServer);
        // call starting methods
        myServer.startServer();
        assertTrue(testHttpServer.createContextCalled);
        assertTrue(testHttpServer.setExecutorCalled);
        assertTrue(testHttpServer.startCalled);
    }

    @Test
    void testServerOffline() {
        ServerCheck checker = new ServerCheck();
        assertFalse(checker.isOnline());
    }

    @Test
    void testServerOnline() throws IOException {
        TestHttpServer testHttpServer = new TestHttpServer();
        MyServer myServer = new MyServer(testHttpServer);
        myServer.startServer();
        ServerCheck checker = new ServerCheck();
        checker.setHostNPort("8.8.8.8", 443); // Googles public DNS : should be true
        assertTrue(checker.isOnline());
    }
}
