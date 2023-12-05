package code.server;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class MockWhisperRequestHandler extends VoiceToText implements HttpHandler {
    public MockWhisperRequestHandler() {
        super(new MockHttpConnection(200));
    }

    public MockWhisperRequestHandler(IHttpConnection connection) {
        super(connection);
    }

    public String processAudio(String type) throws IOException, URISyntaxException {
        if (type.equals("mealType")) {
            // processed correctly
            return "Breakfast";
        } else if (type.equals("ingredients")) {
            return "Chicken, eggs.";
        } else {
            return "Error text";
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "Request received";
        if (method.equals("GET")) {
            response = "Breakfast";
        } else if (method.equals("GET2")) {
            response = "Chicken, eggs.";
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }
}
