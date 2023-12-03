package code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import code.client.Model.*;
import code.server.IRecipeDb;
import code.server.Recipe;

/**
 * Test the delete recipe feature for four distinct test cases:
 * 1. Deleting all of the recipes in the recipe list
 * 2. Deleting a recipe at the top of the recipe list
 * 3. Deleting a recipe at the middle of the recipe list
 * 4. Deleting a recipe at the bottom of the recipe list
 */
public class DeleteRecipeTest {
    private RecipeListDb recipeDb;
    private Recipe r1, r2, r3;

    /**
     * Before each test initialize a RecipeDb with three different recipes
     */
    @BeforeEach
    public void setUp() {
        recipeDb = new RecipeListDb();
        // Create three different recipes
        r1 = new Recipe("French Toast", "Breakfast");
        r2 = new Recipe("Mac and Cheese", "Lunch");
        r3 = new Recipe("Steak and Potatoes", "Dinner");
        // Add the three recipes to the RecipeDb
        recipeDb.add(r1);
        recipeDb.add(r2);
        recipeDb.add(r3);
    }

    /***
     * Test case: Deleting all of the recipes in the list
     * Expected result: The recipe list becomes empty
     */
    @Test
    public void testDeleteAllRecipes() {
        // Check that the size of the recipe list is 3 before deletion
        assertEquals(recipeDb.size(), 3);
        // Delete all of the recipes from the RecipeDb
        recipeDb.remove(r1.getId());
        recipeDb.remove(r2.getId());
        recipeDb.remove(r3.getId());
        // Check that the size of the recipe list is now 0
        assertEquals(recipeDb.size(), 0);
    }

    /**
     * Test case: Deleting a recipe from the top of the recipe list
     * Expected result: All of the other recipes have their indices shifted up by 1
     */
    @Test
    public void testDeleteFirstRecipe() {
        // Check that the size of the recipe list is 3 before deletion
        assertEquals(recipeDb.size(), 3);
        // Delete the recipe at the top of the recipe list
        recipeDb.remove(r1.getId());
        // Check that the size of the recipe list is now 2
        assertEquals(recipeDb.size(), 2);
        // Check that the other recipes had their indices shifted up by 1
        List<Recipe> recipeList = recipeDb.getList();
        Recipe idx0 = recipeList.get(0); // Recipe at index 0 of the new recipe list
        Recipe idx1 = recipeList.get(1); // Recipe at index 1 of the new recipe list
        // Check that the recipe at index 0 was the recipe that was at index 1
        assertEquals(idx0.getTitle(), r2.getTitle());
        assertEquals(idx0.getId(), r2.getId());
        // Check that the recipe at index 1 was the recipe that was at index 2
        assertEquals(idx1.getTitle(), r3.getTitle());
        assertEquals(idx1.getId(), r3.getId());
    }

    /**
     * Test case: Deleting a recipe from the middle of the recipe list
     * Expected result: All recipes that were below the deleted recipe have their
     * indices shifted up by 1
     */
    @Test
    public void testDeleteMiddleRecipe() {
        // Check that the size of the recipe list is 3 before deletion
        assertEquals(recipeDb.size(), 3);
        // Delete the recipe at the middle of the recipe list
        recipeDb.remove(r2.getId());
        // Check that the size of the recipe list is now 2
        assertEquals(recipeDb.size(), 2);
        // Check that only the recipe at the bottom of the list had its index shifted up
        // by 1
        List<Recipe> recipeList = recipeDb.getList();
        Recipe idx0 = recipeList.get(0); // Recipe at index 0 of the new recipe list
        Recipe idx1 = recipeList.get(1); // Recipe at index 1 of the new recipe list
        // Check that the recipe at index 0 is still the recipe that was at index 0
        assertEquals(idx0.getTitle(), r1.getTitle());
        assertEquals(idx0.getId(), r1.getId());
        // Check that the recipe at index 1 was the recipe that was at index 2
        assertEquals(idx1.getTitle(), r3.getTitle());
        assertEquals(idx1.getId(), r3.getId());
    }

    /**
     * Test case: Deleting a recipe from the bottom of the recipe list
     * Expected result: None of the other recipes have their indices shifted
     */
    @Test
    public void testDeleteLastRecipe() {
        // Check that the size of the recipe list is 3 before deletion
        assertEquals(recipeDb.size(), 3);
        // Delete the recipe at the middle of the recipe list
        recipeDb.remove(r3.getId());
        // Check that the size of the recipe list is now 2
        assertEquals(recipeDb.size(), 2);
        // Check that none of the other recipes had their index shifted up by 1
        List<Recipe> recipeList = recipeDb.getList();
        Recipe idx0 = recipeList.get(0); // Recipe at index 0 of the new recipe list
        Recipe idx1 = recipeList.get(1); // Recipe at index 1 of the new recipe list
        // Check that the recipe at index 0 is still the recipe that was at index 0
        assertEquals(idx0.getTitle(), r1.getTitle());
        assertEquals(idx0.getId(), r1.getId());
        // Check that the recipe at index 1 is still the recipe that was at index 1
        assertEquals(idx1.getTitle(), r2.getTitle());
        assertEquals(idx1.getId(), r2.getId());
    }
}
