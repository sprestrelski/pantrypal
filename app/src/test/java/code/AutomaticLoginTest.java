package code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

import code.client.Model.Account;
import code.client.Model.RecipeCSVWriter;
import code.client.View.LoginUI;

import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import java.util.List;

public class AutomaticLoginTest {
    private static AccountCSVWriter writer;
    private StringWriter credentials_csv; // Mock "recipes.csv" file to test save functionality
    private Account account;
    private String expected; // Helper string to store the expected value used for assertEquals

    /**
     * Before running the tests, set up a recipe database and initialize two recipes
     */
    @BeforeEach
    public void setUp() throws IOException {
        account = new Account("GMIRANDA", "CSE110");

    }

    /**
     * Test case: Saving a new recipe to a new recipes.csv file
     * Expected result: A new recipes.csv file is created with the new recipe
     */
    @Test
    public void testSaveUserCredentials() throws IOException {
        credentials_csv = new StringWriter();
        writer = new AccountCSVWriter(credentials_csv);
        writer.writeCredentials(account.getUsername(), account.getPassword());

        expected = "GMIRANDA|CSE110";
        assertEquals(expected, credentials_csv.toString());
    }
}

class AccountCSVWriter {
    public final Writer writer;

    public AccountCSVWriter(Writer writer) {
        this.writer = writer;
    }

    public void writeCredentials(String username, String password) throws IOException {
        writer.append(username)
                .append("|")
                .append(password);
    }
}