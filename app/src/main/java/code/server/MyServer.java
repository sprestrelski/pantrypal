package code.server;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class MyServer {
  // initialize server port and hostname
  private static final int SERVER_PORT = 8100;
  private static final String SERVER_HOSTNAME = "localhost";
  private final HttpServer httpServer;

  public MyServer(HttpServer httpServer) {
    this.httpServer = httpServer;
  }

  public void startServer() {
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    httpServer.createContext("/recipes", new RecipeRequestHandler());
    httpServer.setExecutor(threadPoolExecutor);
    httpServer.createContext("/user", new RequestAccHandler());
    httpServer.start();
    System.out.println("Server started on port " + SERVER_PORT);
  }

  public static void main(String[] args) throws IOException {
    // create a server
    HttpServer server = HttpServer.create(
        new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
        0);
    MyServer myServer = new MyServer(server);
    myServer.startServer();
  }
}
