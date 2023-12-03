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
        StringWriter writer = new StringWriter();
        AccountCSVWriter accountWriter = new AccountCSVWriter(writer);
        accountWriter.writeAccount(account.getUsername(), account.getPassword());
        String expected = "GMIRANDA|CSE110";
        assertEquals(expected, writer.toString());
    }
}