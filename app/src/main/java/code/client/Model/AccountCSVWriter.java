package code.client.Model;

import java.io.IOException;
import java.io.FileWriter;

public class AccountCSVWriter {
    private final FileWriter writer;

    public AccountCSVWriter(FileWriter writer) {
        this.writer = writer;
    }

    public void writeAccount(String username, String password) throws IOException {
        writer.write(username);
        writer.write("|");
        writer.write(password);
    }

    public void close() throws IOException {
        writer.close();
    }
}
