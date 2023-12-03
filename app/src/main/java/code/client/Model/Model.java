package code.client.Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

public class Model {
    public String performAccountRequest(String method, String user, String password) {
        try {
            String urlString = AppConfig.SERVER_URL + AppConfig.ACCOUNT_PATH;
            urlString += "?=" + user + ":" + password;
            URL url = new URI(urlString).toURL();
            // POSSIBLY this later --> "url/username=123&&password=345"
            // Temporarily THIS --> "url/?=123:345"
            // send GET request to see if user exists
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);
            System.out.println("Method is " + method);
            
            // make a new user
            if (method.equals("PUT")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(user + "," + password);
                out.flush();
                out.close();
            }
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    public String performRecipeRequest(String method, String recipe, String userId) {
        // Implement your HTTP request logic here and return the response
        try {
            String urlString = AppConfig.SERVER_URL + AppConfig.RECIPE_PATH;
            if (userId != null) {
                urlString += "?=" + userId;
            }
            
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            if (method.equals("POST") || method.equals("PUT")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(recipe);
                out.flush();
                out.close();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = "";
            String line;
            while ((line = in.readLine()) != null) {
                response += line + "\n";
            }
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }
}