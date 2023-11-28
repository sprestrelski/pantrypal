package code.server;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class AccountRequestHandler implements HttpHandler {
    public AccountRequestHandler() {
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    /*
     * TODO: Expects username and password
     */
    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        if (query != null) {
            String usernameNPassword = query.substring(query.indexOf("=") + 1);

            if (usernameNPassword != null) {

                System.out.println("Queried for "); // TODO
            } else {
                response = "User not found.";
            }
        }

        return response;
    }

    /*
     * TODO FOR accounts
     */
    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        //
        String response = "Posted entry "; // + accountInfo
        System.out.println(response);
        scanner.close();
        return response;
    }

    /*
     * TODO FOR accounts
     */
    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();

        String response = "Added entry ";
        System.out.println(response);
        scanner.close();

        return response;
    }

    /*
     * TODO for deletion.
     */
    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Invalid DELETE request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        if (query != null) {
            String usernameNPassword = query.substring(query.indexOf("=") + 1);

            if (usernameNPassword != null) {

                System.out.println("Queried for "); // TODO
            } else {
                response = "Recipe not found.";
            }
        }

        return response;
    }
}