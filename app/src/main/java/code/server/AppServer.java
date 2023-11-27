package code.server;

import com.sun.net.httpserver.*;

import code.client.Model.AppConfig;
import code.client.Model.IRecipeDb;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class AppServer {
    private final static int NUM_THREADS = 10;
    private HttpServer httpServer;

    public AppServer(IRecipeDb recipeDb) throws IOException {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUM_THREADS);
        // create a map to store data
        // create a server
        httpServer = HttpServer.create(
                new InetSocketAddress(AppConfig.SERVER_HOST, AppConfig.SERVER_PORT),
                0);
        // create the context to map urls
        httpServer.createContext("/recipes", new RecipeRequestHandler(recipeDb));
        // set the executor
        httpServer.setExecutor(threadPoolExecutor);
    }

    public void start() {
        // start the server
        httpServer.start();
        System.out.println("Server started on port " + AppConfig.SERVER_PORT);
    }
}
