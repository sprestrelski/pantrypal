package code.client.View;

import java.net.InetSocketAddress;
import java.net.Socket;

import code.server.BaseServer;

public class ServerConnection {
    private BaseServer server;
    private String ipAddress;
    private int port;

    public ServerConnection(BaseServer server) {
        this.server = server;
        ipAddress = server.getHostName();
        port = server.getPort();
    }

    public ServerConnection(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public BaseServer getServer() {
        return server;
    }

    public void setServer(BaseServer server) {
        this.server = server;
    }

    public boolean isOnline() {
        try {
            InetSocketAddress socketAddr = new InetSocketAddress(ipAddress, port);
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
