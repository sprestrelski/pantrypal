package code;

import java.io.IOException;

import code.server.AppServer;
import code.server.BaseServer;
import code.server.mocking.MockServer;

public class Server {
    private static BaseServer server;

    public static void main(String[] args) throws IOException {
        initServer();
        server.start();
    }

    private static void initServer() throws IOException {
        if (AppConfig.MOCKING_ON) {
            server = new MockServer(AppConfig.SERVER_HOST, AppConfig.SERVER_PORT);
        } else {
            server = new AppServer(AppConfig.SERVER_HOST, AppConfig.SERVER_PORT);
        }
    }

}
