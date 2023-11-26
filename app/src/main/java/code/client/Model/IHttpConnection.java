package code.client.Model;

import java.net.ProtocolException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IHttpConnection {
    int getResponseCode() throws IOException;
    InputStream getInputStream() throws IOException;
    OutputStream getOutputStream() throws IOException;
    InputStream getErrorStream() throws IOException;
    void setRequestMethod(String method) throws ProtocolException;
    void setRequestProperty(String key, String value);
    void setDoOutput(boolean output);
}