package code.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.*;

import code.client.Model.AppConfig;
import code.client.Model.IRecipeDb;
import code.client.Model.RecipeMongoDb;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

import org.bson.Document;

public class AppServer {
    private final static int NUM_THREADS = 10;
    private HttpServer httpServer;
    private static final String DB_NAME = "pantry_pal";
    private static final String COLLECTION_NAME = "recipes";

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

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(AppConfig.MONGODB_CONN_STRING)) {
            MongoDatabase mongoDb = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = mongoDb.getCollection(COLLECTION_NAME);
            IRecipeDb recipeDb = new RecipeMongoDb(collection);
            // RecipeCSVReader csvReader = new RecipeCSVReader(new FileReader(AppConfig.CSV_FILE));
            // csvReader.readRecipeDb(recipeDb);
            // csvReader.close();
            AppServer appServer = new AppServer(recipeDb);
            appServer.start();
            while(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
