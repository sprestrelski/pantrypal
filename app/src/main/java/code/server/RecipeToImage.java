package code.server;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public abstract class RecipeToImage {
    public abstract void handle(HttpExchange httpExchange) throws IOException;
}
