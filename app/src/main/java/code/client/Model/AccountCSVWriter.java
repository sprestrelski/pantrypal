package code.client.Model;

import java.io.IOException;
import java.io.Writer;

public class AccountCSVWriter {
    private final Writer writer;

    public AccountCSVWriter(Writer writer) {
        this.writer = writer;
    }

    public void writeAccount(String username, String password) throws IOException {
        writer.append(username)
                .append("|")
                .append(password);
    }
}
