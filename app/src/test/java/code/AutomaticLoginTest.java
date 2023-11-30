package code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

import code.client.Model.Account;
import code.client.Model.RecipeCSVWriter;
import code.client.View.LoginUI;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import java.util.List;

/**
 * Test the save recipe list feature for three distinct test cases:
 * 1. Saving a new recipe to a new CSV file
 * 2. Saving a new recipe to an existing CSV file
 * 3. Saving changes made to an existing recipe to an existing CSV file
 */
public class AutomaticLoginTest {
    // private RecipeCSVWriter writer; // Recipe writer to write recipes into a
    // mocked "recipes.csv"
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
        // Create a mock "usercredentials.csv" file
        try (FileWriter writer = new FileWriter("userCredentials.csv", true)) {
            writer.append("GMIRANDA")
                    .append("|")
                    .append("CSE110");
            writer.flush();
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            System.out.println("Account credentials could not be saved.");
        }

        // test loginUI
        LoginUI loginUI = new LoginUI();
        assertEquals("GMIRANDA", loginUI.getUsernameTextField().getText());
        assertEquals("CSE110", loginUI.getPasswordField().getText());

        // // Initialize a writer to add saved recipes to "recipes.csv"
        // RecipeCSVWriter writer = new RecipeCSVWriter(credentials_csv);
        // // Initialize recipes_csv file without recipes
        // writer.writeRecipeDb(recipeDb);
        // /**
        // * The recipes.csv file should now look like the following:
        // * sep=|
        // * Recipe Name| Ingredients| Instructions
        // */
        // // Initialize a helper variable to store the expected contents of the CSV
        // file expected = "sep=|\nRecipe Name| Ingredients| Instructions\n";
        // assertEquals(expected, recipes_csv.toString());
        // // Add a new recipe to the recipe database
        // recipeDb.add(r1);
        // // Save the new recipe to the empty recipes.csv file
        // writer.writeRecipeDb(recipeDb);
        // /**
        // * The recipes.csv file should now look like the following:
        // * sep=|
        // * Recipe Name| Ingredients| Instructions
        // * Plain Spaghetti| Spaghetti noodles| Boil the noodles
        // */
        // expected += "Plain Spaghetti| Spaghetti noodles| Boil the noodles\n";
        // assertEquals(expected, recipes_csv.toString());
    }
}