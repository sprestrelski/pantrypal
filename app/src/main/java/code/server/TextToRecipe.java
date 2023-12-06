package code.server;

import com.sun.net.httpserver.*;
import java.io.IOException;

public abstract class TextToRecipe {
    public abstract void handle(HttpExchange httpExchange) throws IOException;

    public abstract void setSampleRecipe(String recipe);
}
