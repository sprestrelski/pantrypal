package code.server;

import java.io.IOException;

public abstract class BaseServer {
    protected final String hostName;
    protected final int port;

    public BaseServer(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public abstract void start() throws IOException;

    public abstract void stop();
}
