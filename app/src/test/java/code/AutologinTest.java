package code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

import code.client.Model.Account;
import code.client.Model.AccountCSVWriter;

import java.io.IOException;
import java.io.StringWriter;

public class AutologinTest {
    private Account account;

    /**
     * Before running the test, set up an account for automatic log in
     */
    @BeforeEach
    public void setUp() throws IOException {
        account = new Account("GMIRANDA", "CSE110");
    }

    /**
     * Save user credentials to a file called userCredentials.csv
     * Expected result: A userCredentials.csv file with the user credentials
     */
    @Test
    public void testSaveUserCredentials() throws IOException {
        StringWriter writer = new StringWriter();
        AccountCSVWriter accountWriter = new AccountCSVWriter(writer);
        accountWriter.writeAccount(account.getUsername(), account.getPassword());
        String expected = "GMIRANDA|CSE110";
        assertEquals(expected, writer.toString());
    }
}