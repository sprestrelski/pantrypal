package code.client.Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

public class Model {

    public String performUserRequest(String method, String user, String password) {
        try {
            String urlString = AppConfig.SERVER_URL + "/user";
            URL url = new URI(urlString).toURL();
            urlString += "?=" + user + ":" + password;
            // POSSIBLY this later --> "url/username=123&&password=345"
            // Temporarily THIS --> "url/?=123:345"

            // send GET request to see if user exists
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);
            
            // make a new user
            if (method.equals("POST")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(user + "," + password);
                out.flush();
                out.close();
            } // return recipe list
            else if (method.equals("GET")) {
                
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

    public String performRecipeRequest(String method, String recipe, String uuid) {
        // Implement your HTTP request logic here and return the response
        try {
            String urlString = AppConfig.SERVER_URL + "/recipes";
            if (uuid != null) {
                urlString += "?=" + uuid;
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
            String response = in.readLine();
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }
}