package code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import code.server.*;
import code.client.Model.RecipeListDb;
import code.client.Model.RecipeCSVWriter;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Test the save recipe list feature for three distinct test cases:
 * 1. Saving a new recipe to a new CSV file
 * 2. Saving a new recipe to an existing CSV file
 * 3. Saving changes made to an existing recipe to an existing CSV file
 */

public class SaveRecipeTest {
        private static RecipeCSVWriter recipeWriter; // Recipe writer to write recipes into a mocked "recipes.csv"
        private static StringWriter writer; // Mock "recipes.csv" file to test save functionality
        private static Recipe r1, r2; // Recipes that will be used for the tests
        private static RecipeListDb recipeDb; // Recipe database for storing test recipes
        private static StringBuilder expected; // Helper string to store the expected value used for assertEquals
        private final static String RECIPE_ID1 = "107c7f79bcf86cd7994f6c0e";
        private final static String RECIPE_ID2 = "107c7f79bcf86cd7994f6c0f";
        private final static String ACCOUNT_ID = "107c7f79bcf86cd7994f6c0e";

        /**
         * Before running the tests, set up a recipe database and initialize two recipes
         */
        @BeforeAll
        public static void setUp() throws IOException {
                // Initialize a RecipeDb
                recipeDb = new RecipeListDb();
                // Initialize two simple recipes
                r1 = new Recipe(RECIPE_ID1, ACCOUNT_ID, "Plain Spaghetti", "breakfast", "error");
                r1.addIngredient("Spaghetti noodles");
                r1.addInstruction("Boil the noodles");
                r2 = new Recipe(RECIPE_ID2, ACCOUNT_ID, "Steak", "lunch", "error");
                r2.addIngredient("Raw beef");
                r2.addInstruction("Cook the beef");
                // Create a mock "recipes.csv" file
                writer = new StringWriter();
                // Initialize a writer to add saved recipes to "recipes.csv"
                recipeWriter = new RecipeCSVWriter(writer);
                // Initialize recipes_csv file without recipes
                recipeWriter.writeRecipeDb(recipeDb);
        }

        /**
         * Test case: Saving a new recipe to a new recipes.csv file
         * Expected result: A new recipes.csv file is created with the new recipe
         */
        @Test
        public void testSaveNewFile() throws IOException {
                /**
                 * The recipes.csv file should now look like the following:
                 * sep=::
                 * ID::Account::Title::Tag::Ingredients::Instructions
                 */
                // Initialize a helper variable to store the expected contents of the CSV file
                expected = new StringBuilder();
                expected.append("sep=::\nID::Account::Title::Tag::Ingredients::Instructions::Image\n");
                assertEquals(expected.toString(), writer.toString());
                // Add a new recipe to the recipe database
                recipeDb.clear();
                recipeDb.add(r1);
                // Save the new recipe to the empty recipes.csv file
                StringWriter strWriter = new StringWriter();
                recipeWriter = new RecipeCSVWriter(strWriter);
                recipeWriter.writeRecipeDb(recipeDb);
                /**
                 * The recipes.csv file should now look like the following:
                 * sep=::
                 * ID::Account::Title::Tag::Ingredients::Instructions::Image
                 * 107c7f79bcf86cd7994f6c0e::107c7f79bcf86cd7994f6c0e::Plain
                 * Spaghetti::breakfast::Spaghetti noodles::Boil the noodles::error
                 */
                expected.append(RECIPE_ID1)
                                .append("::")
                                .append(ACCOUNT_ID)
                                .append("::Plain Spaghetti::")
                                .append("breakfast")
                                .append("::Spaghetti noodles::Boil the noodles::error\n");
                assertEquals(expected.toString(), strWriter.toString());
        }

        /*
         * Test case: Saving a new recipe to an existing recipes.csv file
         * Expected result: An existing recipes.csv file is updated with the new recipe
         */
        @Test
        public void testSaveOldFile() throws IOException {
                // Add a new recipe to the RecipeDb
                recipeDb.clear();
                recipeDb.add(r1);
                recipeDb.add(r2);
                // // Create a new "recipes.csv" file
                // recipes_csv = new StringWriter();
                // // Initialize a writer to add saved recipes to "recipes.csv"
                // writer = new RecipeWriter(recipes_csv);
                // Save the new recipe to the existing recipes.csv file
                StringWriter strWriter = new StringWriter();
                recipeWriter = new RecipeCSVWriter(strWriter);
                recipeWriter.writeRecipeDb(recipeDb);
                /**
                 * The recipes.csv file should look like the following:
                 * sep=::
                 * ID::Account::Title::Tag::Ingredients::Instructions
                 * 107c7f79bcf86cd7994f6c0e::107c7f79bcf86cd7994f6c0e
                 * ::Plain Spaghetti::breakfast::Spaghetti noodles::Boil the noodles
                 * 107c7f79bcf86cd7994f6c0e::107c7f79bcf86cd7994f6c0e::Steak::lunch::Raw
                 * beef::Cook the beef
                 */
                expected = new StringBuilder();
                expected.append("sep=::\nID::Account::Title::Tag::Ingredients::Instructions::Image\n")
                                .append(RECIPE_ID1)
                                .append("::")
                                .append(ACCOUNT_ID)
                                .append("::Plain Spaghetti::")
                                .append("breakfast")
                                .append("::Spaghetti noodles::Boil the noodles::error\n")
                                .append(RECIPE_ID2)
                                .append("::")
                                .append(ACCOUNT_ID)
                                .append("::Steak::")
                                .append("lunch")
                                .append("::Raw beef::Cook the beef::error\n");
                assertEquals(expected.toString(), strWriter.toString());
        }

        /**
         * Test case: Saving changes to an existing recipe to an existing recipes.csv
         * file
         * Expected result: An existing recipes.csv file is updated with the updated
         * recipe
         */
        @Test
        public void testSaveEditOldFile() throws IOException {
                // Edit an existing recipe in the RecipeDb
                recipeDb.clear();
                recipeDb.add(r1);
                recipeDb.add(r2);
                r2.addInstruction("Add steak sauce"); // Edit the recipe by adding an instruction
                // Save the updated recipe to the existing recipes.csv file
                StringWriter strWriter = new StringWriter();
                recipeWriter = new RecipeCSVWriter(strWriter);
                recipeWriter.writeRecipeDb(recipeDb);
                /**
                 * The recipes.csv file should look like the following:
                 * sep=::
                 * ID::Account::Title::Tag::Ingredients::Instructions
                 * 107c7f79bcf86cd7994f6c0e::107c7f79bcf86cd7994f6c0e::Plain
                 * Spaghetti::breakfast::Spaghetti noodles::Boil the noodles
                 * 107c7f79bcf86cd7994f6c0e::107c7f79bcf86cd7994f6c0e
                 * ::Steak::lunch::Raw beef::Cook the beef;;Add steak sauce
                 */
                expected = new StringBuilder();
                expected.append("sep=::\nID::Account::Title::Tag::Ingredients::Instructions::Image\n")
                                .append(RECIPE_ID1)
                                .append("::")
                                .append(ACCOUNT_ID)
                                .append("::Plain Spaghetti::")
                                .append("breakfast")
                                .append("::Spaghetti noodles::Boil the noodles::error\n")
                                .append(RECIPE_ID2)
                                .append("::")
                                .append(ACCOUNT_ID)
                                .append("::Steak::")
                                .append("lunch")
                                .append("::Raw beef::Cook the beef;;Add steak sauce::error\n");
                assertEquals(expected.toString(), strWriter.toString());
        }
}
