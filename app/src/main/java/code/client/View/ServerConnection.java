package code.client.View;

import java.net.InetSocketAddress;
import java.net.Socket;

import code.server.BaseServer;

public class ServerConnection {
    private BaseServer server;

    public ServerConnection(BaseServer server) {
        this.server = server;
    }

    public BaseServer getServer() {
        return server;
    }

    public void setServer(BaseServer server) {
        this.server = server;
    }

    public boolean isOnline() {
        try {
            InetSocketAddress socketAddr = new InetSocketAddress(server.getHostName(), server.getPort());
            Socket socket = new Socket();
            socket.connect(socketAddr, 500);
            socket.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
