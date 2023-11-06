package code.client.Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;


public class Model {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN = "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";
    private static final String MODEL = "whisper-1";

    public String performWhisperRequest() {
        // Implement your HTTP request logic here and return the response
        try {

            WhisperHandler whisper = new WhisperHandler(API_ENDPOINT, TOKEN, MODEL);
            String response = whisper.processAudio();
            return response;

            /*
             * String urlString = "http://localhost:8100/";
             * if (query != null) {
             * urlString += "?=" + query;
             * }
             * URL url = new URI(urlString).toURL();
             * HttpURLConnection conn = (HttpURLConnection) url.openConnection();
             * conn.setRequestMethod(method);
             * conn.setDoOutput(true);
             * 
             * if (method.equals("POST") || method.equals("PUT")) {
             * OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
             * out.write(language + "," + year);
             * out.flush();
             * out.close();
             * }
             * 
             * BufferedReader in = new BufferedReader(new
             * InputStreamReader(conn.getInputStream()));
             * String response = in.readLine();
             * in.close();
             * return response;
             */
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }
}