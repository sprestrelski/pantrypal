package code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import code.server.Recipe;
import code.server.RecipeBuilder;

public class FilterRecipeTest {
    private List<Recipe> mainRecipeList = new ArrayList<>(); // Main recipe list used throughout the tests
    private List<Recipe> expected; // Recipe list to store expected result after filtering
    private RecipeBuilder b1, b2, b3; // Recipe builders used to set up the recipe list
    private Recipe r1, r2, r3; // Recipes that will be used to the tests
    
    /**
     * This method is a replica of our update method in RecipeListUI. It has the
     * same logic, but is rewritten to not create UI elements. Additionally, it
     * adds the elements to a normal List rather than a UI list.
     * 
     * @param filter - the filter criteria selected by the user
     */
    private void update(String filter) {
        List<Recipe> temp = new ArrayList<>();
        for (Recipe recipe : mainRecipeList) {
            if (filter.equals("none") || recipe.getMealTag().toLowerCase().equals(filter.toLowerCase())) {
                temp.add(recipe);
            }
        }
        mainRecipeList = temp;
    }

    @BeforeEach
    public void setUp() {
        // Initialize recipe builders with different meal tags
        b1 = new RecipeBuilder("user", "LunchMeal1");
        b1.setMealTag("lunch");
        b2 = new RecipeBuilder("user", "BreakfastMeal");
        b2.setMealTag("breakfast");                       
        b3 = new RecipeBuilder("user", "LunchMeal2");  
        b3.setMealTag("lunch");                    
        // Use the recipe builders to build the recipes
        r1 = b1.buildRecipe();
        r2 = b2.buildRecipe();
        r3 = b3.buildRecipe();
        // Add the recipes to the recipe list
        mainRecipeList.add(r1);
        mainRecipeList.add(r2);
        mainRecipeList.add(r3);
        // Make expected empty before each test
        expected = new ArrayList<>();
    }

    /**
     * Remove selected filters from the recipe list
     * Expected list: r1, r2, r3
     */
    @Test
    public void testNoFilter() {
        // Recipe list size should be 3 before filtering
        assertTrue(mainRecipeList.size() == 3);
        // Filter the recipe list to show only breakfast recipes
        update("breakfast");
        // Filtered list should only have recipe r2
        expected.add(r2);
        assertEquals(expected, mainRecipeList);
        // Recipe list size should be 1 after filtering
        assertTrue(mainRecipeList.size() == 1);
        // Add all of the recipes back to the recipe list
        mainRecipeList.clear();
        mainRecipeList.add(r1);
        mainRecipeList.add(r2);
        mainRecipeList.add(r3);
        // Remove the selected filter
        update("none");
        // Filtered list should have r1, r2, and r3
        expected.clear();
        expected.add(r1);
        expected.add(r2);
        expected.add(r3);
        assertEquals(expected, mainRecipeList);
        // Recipe list size should be 3 after removing the filters
        assertTrue(mainRecipeList.size() == 3);
    }

    /**
     * Filter a recipe list that contains one match to the filter criteria
     * Expected list: r2
     */
    @Test
    public void testOneMatchFilter() {
        // Recipe list size should be 3 before filtering
        assertTrue(mainRecipeList.size() == 3);
        // Filter the recipe list to show only breakfast recipes
        update("breakfast");
        // Filtered recipe list should only have r2
        expected.add(r2);
        assertEquals(expected, mainRecipeList);
        // Recipe list size should be 1 after filtering
        assertTrue(mainRecipeList.size() == 1);
    }

    /**
     * Filter a recipe list that contains multiple matches to the filter criteria
     * Expected list: r1, r3
     */
    @Test
    public void testMultipleMatchFilter() {
        // Recipe list size should be 3 before filtering
        assertTrue(mainRecipeList.size() == 3);
        // Filter the recipe list to show only lunch recipes
        update("lunch");
        // Filtered recipe list should only have r1 and r3
        expected.add(r1);
        expected.add(r3);
        assertEquals(expected, mainRecipeList);
        // Recipe list size should be 2 after filtering
        assertTrue(mainRecipeList.size() == 2);
    }

    /**
     * Filter a recipe list that contains no matches to the filter criteria
     * Expected list: empty
     */
    @Test
    public void testNoMatchFilter() {
        // Recipe list size should be 3 before filtering
        assertTrue(mainRecipeList.size() == 3);
        // Filter the recipe list to show only dinner recipes
        update("dinner");
        // Filtered recipe list should be empty
        assertEquals(expected, mainRecipeList);
        // Recipe list size should be 0 after filtering
        assertTrue(mainRecipeList.isEmpty());
    }
}
