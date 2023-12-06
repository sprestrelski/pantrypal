package code.server;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AppHttpConnection implements IHttpConnection {
    private HttpURLConnection connection;

    public AppHttpConnection(String apiEndPoint)
            throws URISyntaxException, IOException {
        URL url = new URI(apiEndPoint).toURL();
        this.connection = (HttpURLConnection) url.openConnection();
    }

    @Override
    public int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return connection.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return connection.getOutputStream();
    }

    @Override
    public InputStream getErrorStream() throws IOException {
        return connection.getErrorStream();
    }

    @Override
    public void setRequestMethod(String method) throws ProtocolException {
        connection.setRequestMethod(method);
    }

    @Override
    public void setRequestProperty(String key, String value) {
        connection.setRequestProperty(key, value);
    }

    @Override
    public void setDoOutput(boolean output) {
        connection.setDoOutput(output);
    }
}