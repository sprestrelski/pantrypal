package code.server;

import java.io.IOException;

public class MockServer extends BaseServer {
    public MockServer(String hostName, int port) {
        super(hostName, port);
    }

    @Override
    public void start() throws IOException {
    }
}
