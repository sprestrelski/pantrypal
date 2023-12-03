package code;

import org.junit.jupiter.api.Test;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;

import code.client.Model.Recipe;
import code.client.Model.RecipeListDb;
import code.client.Model.RecipeCSVWriter;

import java.io.IOException;
import java.io.StringWriter;

import java.util.List;

/**
 * Test the save recipe list feature for three distinct test cases:
 * 1. Saving a new recipe to a new CSV file
 * 2. Saving a new recipe to an existing CSV file
 * 3. Saving changes made to an existing recipe to an existing CSV file
 */
public class SaveRecipeTest {
    private static RecipeCSVWriter writer; // Recipe writer to write recipes into a mocked "recipes.csv"
    private static StringWriter recipes_csv; // Mock "recipes.csv" file to test save functionality
    private static Recipe r1, r2; // Recipes that will be used for the tests
    private static RecipeListDb recipeDb; // Recipe database for storing test recipes
    private static String expected; // Helper string to store the expected value used for assertEquals
    private final static String RECIPE_ID = "107c7f79bcf86cd7994f6c0e";
    private final static String ACCOUNT_ID = "107c7f79bcf86cd7994f6c0e";

    /**
     * Before running the tests, set up a recipe database and initialize two recipes
     */
    @BeforeAll
    public static void setUp() throws IOException {
        // Initialize a RecipeDb
        recipeDb = new RecipeListDb();
        // Initialize two simple recipes
        r1 = new Recipe(new ObjectId(RECIPE_ID), new ObjectId(ACCOUNT_ID), "Plain Spaghetti", "Breakfast", null);
        r1.addIngredient("Spaghetti noodles");
        r1.addInstruction("Boil the noodles");
        r2 = new Recipe(new ObjectId(RECIPE_ID), new ObjectId(ACCOUNT_ID), "Steak", "Lunch", null);
        r2.addIngredient("Raw beef");
        r2.addInstruction("Cook the beef");
    }

    /**
     * Test case: Saving a new recipe to a new recipes.csv file
     * Expected result: A new recipes.csv file is created with the new recipe
     */
    @Test
    public static void testSaveNewFile() throws IOException {
        // Create a mock "recipes.csv" file
        recipes_csv = new StringWriter();
        // Initialize a writer to add saved recipes to "recipes.csv"
        writer = new RecipeCSVWriter(recipes_csv);
        // Initialize recipes_csv file without recipes
        writer.writeRecipeDb(recipeDb);
        /**
         * The recipes.csv file should now look like the following:
         * sep=::
         * ID::Account::Title::Tag::Ingredients::Instructions
         */
        // Initialize a helper variable to store the expected contents of the CSV file
        expected = "sep=::\nID::Account::Title::Tag::Ingredients::Instructions\n";
        assertEquals(expected, recipes_csv.toString());
        // Add a new recipe to the recipe database
        recipeDb.add(r1);
        // Save the new recipe to the empty recipes.csv file
        writer.writeRecipeDb(recipeDb);
        /**
         * The recipes.csv file should now look like the following:
         * sep=::
         * ID::Account::Title::Tag::Ingredients::Instructions
         * 107c7f79bcf86cd7994f6c0e::107c7f79bcf86cd7994f6c0e::Plain
         * 
         * Spaghetti::Spaghetti noodles::Boil the noodles
         */
        expected += RECIPE_ID + "::" + ACCOUNT_ID
                + "::Plain Spaghetti::Breakfast::Spaghetti noodles::Boil the noodles\n";
        assertEquals(expected, recipes_csv.toString());
    }

    /*
     * Test case: Saving a new recipe to an existing recipes.csv file
     * Expected result: An existing recipes.csv file is updated with the new recipe
     */
    @Test
    public static void testSaveOldFile() throws IOException {
        // Add a new recipe to the RecipeDb
        recipeDb.add(r2);
        // // Create a new "recipes.csv" file
        // recipes_csv = new StringWriter();
        // // Initialize a writer to add saved recipes to "recipes.csv"
        // writer = new RecipeWriter(recipes_csv);
        // Save the new recipe to the existing recipes.csv file
        writer.writeRecipeDb(recipeDb);
        /**
         * The recipes.csv file should look like the following:
         * sep=::
         * ID::Account::Title::Tag::Ingredients::Instructions
         * 107c7f79bcf86cd7994f6c0e::107c7f79bcf86cd7994f6c0e
         * ::Plain
         * Spaghetti::Spaghetti noodles::Boil the noodles
         * 107c7f79bcf86cd7994f6c0e::107c7f79bcf86cd7994f6c0e::Steak::Raw beef::Cook the
         * beef
         */
        expected = "sep=::\nID::Account::Title::Tag::Ingredients::Instructions\n";
        expected += RECIPE_ID + "::" + ACCOUNT_ID + "::Plain Spaghetti::Lunch::Spaghetti noodles::Boil the noodles\n";
        expected += RECIPE_ID + "::" + ACCOUNT_ID + "::Steak::Raw beef::Cook the beef\n";
        assertEquals(expected, recipes_csv.toString());
    }

    /**
     * Test case: Saving changes to an existing recipe to an existing recipes.csv
     * file
     * Expected result: An existing recipes.csv file is updated with the updated
     * recipe
     */
    @Test
    public static void testSaveEditOldFile() throws IOException {
        // Edit an existing recipe in the RecipeDb
        List<Recipe> recipeList = recipeDb.getList();
        recipeList.get(1).addInstruction("Add steak sauce"); // Edit the recipe by adding an instruction
        // Save the updated recipe to the existing recipes.csv file
        writer.writeRecipeDb(recipeDb);
        /**
         * The recipes.csv file should look like the following:
         * sep=::
         * ID::Account::Title::Tag::Ingredients::Instructions
         * 107c7f
         * 79bcf86cd7994f6c0e::107c7f79bcf86cd7994f6c0e::Plain
         * Spaghetti::Spaghetti noodles::Boil the noodles
         * 107c7f79bcf86cd7994f6c0e::107c7f79bcf86cd7994f6c0e
         * ::Steak::Raw beef::Cook the
         * beef;;Add steak sauce
         */
        expected = "sep=::\nID::Account::Title::Tag::Ingredients::Instructions\n";
        expected += RECIPE_ID + "::" + ACCOUNT_ID + "::Plain Spaghetti::Spaghetti noodles::Boil the noodles\n";
        expected += RECIPE_ID + "::" + ACCOUNT_ID + "::Steak::Raw beef::Cook the beef;;Add steak sauce\n";
        assertEquals(expected, recipes_csv.toString());
    }
}
