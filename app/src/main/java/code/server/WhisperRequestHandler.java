package code.server;

import com.sun.net.httpserver.*;

import code.client.Model.AppConfig;
import code.client.Model.AppHttpConnection;
import java.io.*;
import java.net.*;

public class WhisperRequestHandler extends VoiceToText implements HttpHandler {
    public static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    public static final String MODEL = "whisper-1";

    public WhisperRequestHandler() throws URISyntaxException, IOException {
        super(new AppHttpConnection(API_ENDPOINT));
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request received";
        String method = httpExchange.getRequestMethod();
        System.out.println("Method is " + method);
        int audioFileSize = 0;

        try {
            File audioFile = new File(AppConfig.AUDIO_FILE);

            if (!audioFile.exists()) {
                audioFile.createNewFile();
            }

            InputStream multipartInStream = httpExchange.getRequestBody();
            String nextLine = "";

            do {
                nextLine = readLine(multipartInStream, "\r\n");
                if (nextLine.startsWith("Content-Length:")) {
                    audioFileSize = Integer.parseInt(
                            nextLine.replaceAll(" ", "").substring(
                                    "Content-Length:".length()));
                }
            } while (!nextLine.equals(""));

            byte[] audioByteArray = new byte[audioFileSize];
            int readOffset = 0;

            while (readOffset < audioFileSize) {
                int bytesRead = multipartInStream.read(audioByteArray, readOffset, audioFileSize);
                readOffset += bytesRead;
            }

            BufferedOutputStream audioOutStream = new BufferedOutputStream(new FileOutputStream(AppConfig.AUDIO_FILE));
            audioOutStream.write(audioByteArray, 0, audioFileSize);
            audioOutStream.flush();
            audioOutStream.close();
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

    private static String readLine(InputStream multipartInStream, String lineSeparator) throws IOException {
        int offset = 0, i = 0;
        byte[] separator = lineSeparator.getBytes("UTF-8");
        byte[] lineBytes = new byte[1024];

        while (multipartInStream.available() > 0) {
            int nextByte = multipartInStream.read();
            if (nextByte < -1) {
                throw new IOException("Reached end of stream while reading the current line!");
            }

            lineBytes[i] = (byte) nextByte;

            if (lineBytes[i++] == separator[offset++]) {
                if (offset == separator.length) {
                    return new String(lineBytes, 0, i - separator.length, "UTF-8");
                }
            } else {
                offset = 0;
            }

            if (i == lineBytes.length) {
                throw new IOException("Maximum line length exceeded: " + i);
            }
        }

        throw new IOException("Reached end of stream while reading the current line!");
    }

    // https://stackoverflow.com/questions/25334139/how-to-mock-a-url-connection
    public String processAudio(String type) throws IOException, URISyntaxException {
        // Send HTTP request
        sendHttpRequest();
        return super.processAudio(type);
    }

    private void sendHttpRequest() throws IOException, URISyntaxException {
        // Set up request headers
        File file = new File(AppConfig.AUDIO_FILE);
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("Authorization", "Bearer " + AppConfig.API_KEY);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set up output stream to write request body
        OutputStream outputStream = connection.getOutputStream();
        // Write model parameter to request body
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
                ("Content-Disposition: form-data; name=\"model\"\r\n\r\n").getBytes());
        outputStream.write((MODEL + "\r\n").getBytes());
        // Write file parameter to request body
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" +
                file.getName() +
                "\"\r\n").getBytes());
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        fileInputStream.close();
        // Write closing boundary to request body
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
        // Flush and close output stream
        outputStream.flush();
        outputStream.close();
    }
}