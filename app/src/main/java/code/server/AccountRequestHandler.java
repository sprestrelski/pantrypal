package code.server;

import com.mongodb.MongoSocketReadException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.MongoWriteException;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class AccountRequestHandler implements HttpHandler {
    private IAccountDb accountDB;
    public static final String USERNAME_NOT_FOUND = "Username is not found";
    public static final String INCORRECT_PASSWORD = "Incorrect password";
    public static final String TAKEN_USERNAME = "Username has already been taken";

    public AccountRequestHandler(IAccountDb accountMongoDB) {
        this.accountDB = accountMongoDB;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request received";
        String method = httpExchange.getRequestMethod();
        System.out.println("Method is " + method);

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            } else {
                throw new Exception("Not valid request method.");
            }
        } catch (MongoWriteException ex) {
            ex.printStackTrace();
            response = "Duplicate Key Error";
        } catch (ConnectException e) {
            response = "Server Offline";
        } catch (Exception e) {
            response = "Error";
            System.out.println("An erroneous request");
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        if (query != null) {
            String value = query.substring(query.indexOf("=") + 1);
            System.out.println("Value is: " + value);
            if (value != null) {
                String[] userNamePassword = value.split(":");
                String username = userNamePassword[0];
                String password = userNamePassword[1];
                Account account = accountDB.findByUsername(username);

                if (account == null) {
                    response = USERNAME_NOT_FOUND;
                } else if (!accountDB.checkPassword(username, password)) {
                    response = INCORRECT_PASSWORD;
                } else {
                    response = account.getId();
                }
            }
        }

        return response;
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String username = postData.substring(0, postData.indexOf(",")),
                password = postData.substring(postData.indexOf(",") + 1);
        Account account = new Account(username, password);
        String response;

        if (accountDB.findByUsername(username) == null) {
            accountDB.add(account);
            response = "Added account " + username;
        } else {
            response = TAKEN_USERNAME;
        }

        scanner.close();
        return response;
    }
}