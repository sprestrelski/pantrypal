package code.client.Model;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

public class ServerCheck {
    private int SERVER_PORT = 8100;
    private String SERVER_HOSTNAME = "localhost";

    public boolean isOnline() {
        boolean b = true;
        try {
            InetSocketAddress sa = new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT);
            Socket ss = new Socket();
            ss.connect(sa, 500);
            ss.close();
        } catch (Exception e) {
            b = false;
        }
        return b;
    }

    public void setHostNPort(String host, int port) {
        SERVER_HOSTNAME = host;
        SERVER_PORT = port;
    }
}
