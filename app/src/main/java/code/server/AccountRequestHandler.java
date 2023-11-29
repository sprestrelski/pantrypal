package code.server;

import com.mongodb.MongoTimeoutException;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class AccountRequestHandler implements HttpHandler {
    private AccountMongoDB accountMongoDB;

    public AccountRequestHandler(AccountMongoDB accountMongoDB) {
        this.accountMongoDB = accountMongoDB;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        System.out.println("Method is " + method);
        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            } else {
                // response = handleGet(httpExchange);
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
        System.out.println("URI: " + uri);
        String query = uri.getRawQuery();
        System.out.println("Made it to server");///////////////
        System.out.println("Query: " + query);
        if (query != null) {
            String usernameNPassword = query.substring(query.indexOf("=") + 1);
            response = "User not found.";
            if (usernameNPassword != null) {
                String[] split = usernameNPassword.split(":");

                System.out.println("Queried for " + split[0]);
                System.out.println("Split size is " + split.length);

                // If only provided username
                try {
                    if (split.length < 2) {
                        Account takenUsername = accountMongoDB.find(split[0]);
                        response = (takenUsername == null) ? "Username is not taken" : "Username is taken";
                    } else {
                        if (accountMongoDB.validate(split[0], split[1])) {
                            response = "Username and Password are correct.";
                        } else {
                            response = "Password is not correct.";
                        }
                    }
                } catch (MongoTimeoutException e) {
                    response = "No access to database";
                    System.out.println(response);
                }
            }
        }

        return response;
    }

    /*
     * TODO FOR accounts
     */
    private String handlePut(HttpExchange httpExchange) throws IOException {
        String response = "Username has already been taken.";

        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String username = postData.substring(0, postData.indexOf(",")),
                password = postData.substring(postData.indexOf(",") + 1);

        Account account = new Account(username, password);
        if (accountMongoDB.find(username) == null) {
            accountMongoDB.add(account);
            response = "Added entry " + username + ".";
        }

        System.out.println(response);

        scanner.close();

        return response;
    }

}