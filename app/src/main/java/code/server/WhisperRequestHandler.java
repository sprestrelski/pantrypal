package code.server;

import com.sun.net.httpserver.*;

import code.AppConfig;

import java.io.*;
import java.net.URI;

public class WhisperRequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request received";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String type = query.substring(query.indexOf("=") + 1);

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
            VoiceToText whisperService = new WhisperService();
            response = whisperService.processAudio("");
        } catch (Exception e) {
            response = "Error";
            System.out.println("An erroneous request");
            e.printStackTrace();
        }

        if (type.equals("mealType")) {
            response = response.toUpperCase();
            if (response.contains("BREAKFAST")) {
                response = "Breakfast";
            } else if (response.contains("LUNCH")) {
                response = "Lunch";
            } else if (response.contains("DINNER")) {
                response = "Dinner";
            } else {
                response = "Error";
            }
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
}