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
            System.out.print("Server is online");
            return true;
        } catch (Exception e) {
            System.out.print("Server is offline");
            return false;
        }
    }
}
