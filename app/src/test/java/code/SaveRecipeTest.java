package code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;

import code.client.Model.Recipe;
import code.client.Model.RecipeDb;
import code.client.Model.RecipeWriter;

import java.io.IOException;
import java.io.StringWriter;

import java.util.Iterator;

/**
 * Test the save recipe list feature for three distinct test cases:
 * 1. Saving a new recipe to a new CSV file
 * 2. Saving a new recipe to an existing CSV file
 * 3. Saving changes made to an existing recipe to an existing CSV file
 */
public class SaveRecipeTest {
    private static RecipeWriter writer; // Recipe writer to write recipes into a mocked "recipes.csv"
    private static StringWriter recipes_csv; // Mock "recipes.csv" file to test save functionality
    private static Recipe r1, r2; // Recipes that will be used for the tests
    private static RecipeDb recipeDb; // Recipe database for storing test recipes
    private static String expected; // Helper string to store the expected value used for assertEquals

    /**
     * Before running the tests, set up a recipe database and initialize two recipes
     */
    @BeforeAll
    public static void setUp() throws IOException {
        // Initialize a RecipeDb
        recipeDb = new RecipeDb();
        // Initialize two simple recipes
        r1 = new Recipe("Plain Spaghetti");
        r1.addIngredient("Spaghetti noodles");
        r1.addInstruction("Boil the noodles");
        r2 = new Recipe("Steak");
        r2.addIngredient("Raw beef");
        r2.addInstruction("Cook the beef");
    }

    /**
     * Test case: Saving a new recipe to a new recipes.csv file
     * Expected result: A new recipes.csv file is created with the new recipe
     */
    @Test
    public static void testSaveNewFile() throws IOException {
        // Create a new "recipes.csv" file
        recipes_csv = new StringWriter();
        // Initialize a writer to add saved recipes to "recipes.csv"
        writer = new RecipeWriter(recipes_csv);
        // Initialize an empty recipes_csv file
        writer.writeRecipeDb(recipeDb);
        /**
         * The recipes.csv file should now look like the following:
         * sep=|
         * Recipe Name| Ingredients| Instructions
         */
        // Initialize a helper variable to store the expected contents of the CSV file
        expected = "sep=|\nRecipe Name| Ingredients| Instructions\n";
        assertEquals(expected, recipes_csv.toString());
        // Add a new recipe to the recipe database
        recipeDb.add(r1);
        // Save the new recipe to the empty recipes.csv file
        writer.writeRecipeDb(recipeDb);
        /**
         * The recipes.csv file should now look like the following:
         * sep=|
         * Recipe Name| Ingredients| Instructions
         * Plain Spaghetti| Spaghetti noodles| Boil the noodles
         */
        expected += "Plain Spaghetti| Spaghetti noodles| Boil the noodles\n";
        assertEquals(expected, recipes_csv.toString());
    }

    /*
     * Test case: Saving a new recipe to an existing recipes.csv file
     * Expected result: An existing recipes.csv file is updated with the new
     * recipe
     */
    @Test
    public static void testSaveOldFile() throws IOException {
        // // Create a new "recipes.csv" file
        // recipes_csv = new StringWriter();
        // // Initialize a writer to add saved recipes to "recipes.csv"
        // writer = new RecipeWriter(recipes_csv);
        // // Initialize an empty recipes_csv file
        // writer.writeRecipeDb(recipeDb);
        // /**
        // * The recipes.csv file should now look like the following:
        // * sep=|
        // * Recipe Name| Ingredients| Instructions
        // */
        // // Initialize a helper variable to store the expected contents of the CSV
        // file expected = "sep=|\nRecipe Name| Ingredients| Instructions\n";
        // assertEquals(expected, recipes_csv.toString());
        // // Add a new recipe to the RecipeDb
        // recipeDb.add(r1);
        // // Save the new recipe to the empty recipes.csv file
        // recipes_csv = new StringWriter();
        // // Initialize a writer to add saved recipes to "recipes.csv"
        // writer = new RecipeWriter(recipes_csv);
        // writer.writeRecipeDb(recipeDb);
        // /**
        // * The recipes.csv file should now look like the following:
        // * sep=|
        // * Recipe Name| Ingredients| Instructions
        // * Plain Spaghetti| Spaghetti noodles| Boil the noodles
        // */
        // expected += "Plain Spaghetti| Spaghetti noodles| Boil the noodles\n";
        // assertEquals(expected, recipes_csv.toString());

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
         * sep=|
         * Recipe Name| Ingredients| Instructions
         * Plain Spaghetti| Spaghetti noodles| Boil the noodles
         * Steak| Raw beef| Cook the beef
         */
        expected = "sep=|\nRecipe Name| Ingredients| Instructions\n";
        expected += "Plain Spaghetti| Spaghetti noodles| Boil the noodles\n";
        expected += "Steak| Raw beef| Cook the beef\n";
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
        // // Create a new "recipes.csv" file
        // recipes_csv = new StringWriter();
        // // Initialize a writer to add saved recipes to "recipes.csv"
        // writer = new RecipeWriter(recipes_csv);
        // writer.writeRecipeDb(recipeDb);
        // /**
        // * The recipes.csv file should now look like the following:
        // * sep=|
        // * Recipe Name| Ingredients| Instructions
        // */
        // // Initialize a helper variable to store the expected contents of the CSV
        // file expected = "sep=|\nRecipe Name| Ingredients| Instructions\n";
        // assertEquals(expected, recipes_csv.toString());
        // // Add a new recipe to the RecipeDb
        // recipeDb.add(r1);
        // recipes_csv = new StringWriter();
        // // Initialize a writer to add saved recipes to "recipes.csv"
        // writer = new RecipeWriter(recipes_csv);
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

        // // Add a new recipe to the RecipeDb
        // recipeDb.add(r2);
        // // Create a new "recipes.csv" file
        // recipes_csv = new StringWriter();
        // // Initialize a writer to add saved recipes to "recipes.csv"
        // writer = new RecipeWriter(recipes_csv);
        // // Save the new recipe to the existing recipes.csv file
        // writer.writeRecipeDb(recipeDb);
        // /**
        // * The recipes.csv file should look like the following:
        // * sep=|
        // * Recipe Name| Ingredients| Instructions
        // * Plain Spaghetti| Spaghetti noodles| Boil the noodles
        // * Steak| Raw beef| Cook the beef
        // */
        // expected += "Steak| Raw beef| Cook the beef\n";
        // assertEquals(expected, recipes_csv.toString());

        // Edit an existing recipe in the RecipeDb
        Iterator<Recipe> itr = recipeDb.iterator();
        itr.next();
        itr.next().addInstruction("Add steak sauce");
        // // Create a new "recipes.csv" file
        // recipes_csv = new StringWriter();
        // // Initialize a writer to add saved recipes to "recipes.csv"
        // writer = new RecipeWriter(recipes_csv);
        // Save the updated recipe to the existing recipes.csv file
        writer.writeRecipeDb(recipeDb);
        /**
         * The recipes.csv file should look like the following:
         * sep=|
         * Recipe Name| Ingredients| Instructions
         * Plain Spaghetti| Spaghetti noodles| Boil the noodles
         * Steak| Raw beef| Cook the beef;;Add steak sauce
         */
        expected = "sep=|\nRecipe Name| Ingredients| Instructions\n";
        expected += "Plain Spaghetti| Spaghetti noodles| Boil the noodles\n";
        expected += "Steak| Raw beef| Cook the beef;;Add steak sauce\n";
        assertEquals(expected, recipes_csv.toString());
    }
}
