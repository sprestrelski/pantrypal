package code.server;

import com.sun.net.httpserver.*;

import code.client.Model.IRecipeDb;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class AppServer extends BaseServer {
    private IRecipeDb recipeDb;
    private final static int NUM_THREADS = 10;
    private HttpServer httpServer;

    public AppServer(IRecipeDb recipeDb, String hostName, int port) {
        super(hostName, port);
        this.recipeDb = recipeDb;
    }

    @Override
    public void start() throws IOException {
        // Initialize a thread pool
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUM_THREADS);
        // create a map to store data
        // create a server
        httpServer = HttpServer.create(
                new InetSocketAddress(hostName, port),
                0);
        // create the context to map urls
        httpServer.createContext("/recipes", new RecipeRequestHandler(recipeDb));
        httpServer.createContext("/user", new AccountRequestHandler());
        // set the executor
        httpServer.setExecutor(threadPoolExecutor);
        // start the server
        httpServer.start();
        System.out.println("Server started on port " + port);
    }
}
