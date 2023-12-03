package code.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.*;

import code.client.Model.AppConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

import org.bson.Document;

public class AppServer extends BaseServer {
    private IRecipeDb recipeDb;
    private AccountMongoDB accountMongoDB;

    private final static int NUM_THREADS = 10;
    private HttpServer httpServer;

    public AppServer(String hostName, int port) {
        super(hostName, port);
        MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN);
        MongoDatabase mongoDb = mongoClient.getDatabase(AppConfig.MONGO_DB);
        MongoCollection<Document> userCollection = mongoDb.getCollection(AppConfig.MONGO_USER_COLLECTION);
        MongoCollection<Document> recipeCollection = mongoDb.getCollection(AppConfig.MONGO_RECIPE_COLLECTION);
        accountMongoDB = new AccountMongoDB(userCollection);
        recipeDb = new RecipeMongoDb(recipeCollection);
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
        httpServer.createContext(AppConfig.RECIPE_PATH, new RecipeRequestHandler(recipeDb));
        httpServer.createContext(AppConfig.ACCOUNT_PATH, new AccountRequestHandler(accountMongoDB));
        httpServer.createContext(AppConfig.SHARE_PATH, new ShareRequestHandler(accountMongoDB, recipeDb));
        // set the executor
        httpServer.setExecutor(threadPoolExecutor);
        // start the server
        httpServer.start();
        System.out.println("Server started on port " + port);
    }
}
