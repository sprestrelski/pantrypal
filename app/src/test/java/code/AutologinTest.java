package code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

import code.client.Model.Account;
import code.client.Model.AccountCSVWriter;
import code.client.Model.AccountCSVReader;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;

import java.util.List;
import java.util.ArrayList;

/**
 * Test file for testing the automatic login feature. Specifically tests whether
 * or
 * not a username and password are correctly written to a UserCredentials.csv
 * file
 */
public class AutologinTest {
    private Account account; // Account used for the test
    private AccountCSVWriter writer; // Writer that saves user credentials to a csv file
    private AccountCSVReader reader; // Reader that reads user credentials from a csv file
    private List<String> userCredentials; // Helper variable that stores credentials read from the csv file

    /**
     * Before running the test, set up an account with a username and password. Also
     * initialize the userCredentials to an empty list
     */
    @BeforeEach
    public void setUp() throws IOException {
        account = new Account("Chef", "Caitlyn");
        userCredentials = new ArrayList<>();
    }

    /**
     * Save user credentials to a file called UserCredentialsTest.csv
     * Expected result: A UserCredentialsTest.csv file with the user credentials
     */
    @Test
    public void testSaveUserCredentials() throws IOException {
        // Save the username and password to a file called "UserCredentialsTest.csv"
        writer = new AccountCSVWriter(new FileWriter("UserCredentialsTest.csv"));
        writer.writeAccount(account.getUsername(), account.getPassword());
        writer.close();
        // Read the username and password from a file called "UserCredentialsTest.csv"
        reader = new AccountCSVReader(new FileReader("UserCredentialsTest.csv"));
        userCredentials = reader.readUserCredentials();
        reader.close();
        // Check that the username and password were correctly saved to the csv file
        String expectedUsername = "Chef";
        String expectedPassword = "Caitlyn";
        assertEquals(expectedUsername, userCredentials.get(0));
        assertEquals(expectedPassword, userCredentials.get(1));
    }
}