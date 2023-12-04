package code.client.Model;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class AccountCSVReader {
    private final BufferedReader reader;

    public AccountCSVReader(FileReader fileReader) {
        this.reader = new BufferedReader(fileReader);
    }

    public List<String> readUserCredentials() throws IOException {
        // Helper variable to store a line of the user credentials file
        String line;
        // Helper variable to store the return value and store user credentials
        List<String> toReturn = new ArrayList<>();
        // Helper variable to store the username and password in the csv file
        String[] userCredentials;
        // Read the lines of the UserCredentials.csv file
        while ((line = reader.readLine()) != null) {
            userCredentials = line.split("\\|");
            toReturn.add(userCredentials[0]);
            toReturn.add(userCredentials[1]);
        }
        return toReturn;
    }

    public void close() throws IOException {
        reader.close();
    }
}