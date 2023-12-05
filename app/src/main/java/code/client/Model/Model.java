package code.client.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URI;
import java.nio.file.*;

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

            if (method.equals("POST") || method.equals("PUT") || method.equals("DELETE")) {
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

    public String performChatGPTRequest(String method, String mealType, String ingredients) {
        try {
            String urlString = AppConfig.SERVER_URL + AppConfig.CHATGPT_PATH;
            urlString += "?=" + mealType + "::" + ingredients;
            URL url = new URI(urlString).toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);
            // System.out.println("Method is " + method);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    public String performDallERequest(String method, String recipeTitle) {
        try {
            String urlString = AppConfig.SERVER_URL + AppConfig.DALLE_PATH;
            urlString += "?=" + recipeTitle;
            URL url = new URI(urlString).toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);
            // System.out.println("Method is " + method);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    public String performWhisperRequest(String method, String type) throws MalformedURLException, IOException {
        String response = "Unable to perform Whisper request";
        final String postUrl = AppConfig.SERVER_URL + AppConfig.WHISPER_PATH;
        final File audioFile = new File(AppConfig.AUDIO_FILE);
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";
        String charset = "UTF-8";
        URLConnection connection = new URL(postUrl).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);) {
            writer.append("--" + boundary).append(CRLF);
            writer.append(
                    "Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + audioFile.getName() + "\"")
                    .append(CRLF);
            writer.append("Content-Length: " + audioFile.length()).append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(audioFile.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            Files.copy(audioFile.toPath(), output);
            output.flush();
            int responseCode = ((HttpURLConnection) connection).getResponseCode();
            response = ((HttpURLConnection) connection).getResponseMessage();
            System.out.println("Response code: [" + responseCode + "]");

            if (type.equals("mealType") && responseCode == 200) {
                response = response.toUpperCase();
                if (response.contains("BREAKFAST")) {
                    response = "Breakfast";
                } else if (response.contains("LUNCH")) {
                    response = "Lunch";
                } else if (response.contains("DINNER")) {
                    response = "Dinner";
                } else {
                    response = null;
                }
            }
        }

        return response;
    }
}