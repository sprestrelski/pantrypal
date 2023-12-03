package code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import code.server.Recipe;
import code.server.RecipeBuilder;
import code.client.Model.RecipeSorter;

/**
 * Test the sort recipe list feature for four distinct cases:
 * 1. Sort the recipe list in ascending chronological order
 * 2. Sort the recipe list in descending chronological order
 * 3. Sort the recipe list in ascending alphabetical order
 * 4. Sort the recipe list in descending chronological order
 */
public class SortRecipeListTest {
    private List<Recipe> recipeList;
    private RecipeSorter sorter;
    private RecipeBuilder b1, b2, b3, b4;
    private Recipe r1, r2, r3, r4;

    @BeforeEach
    public void setUp() {
        // Initialize an empty recipe list
        recipeList = new ArrayList<>();
        // Initialize a recipe list sorter for the empty recipe list
        sorter = new RecipeSorter(recipeList);
        // Initialize recipe builders that specify only the title and date
        b1 = new RecipeBuilder("user", "recipeOne");   // Alphabetically 2nd
        b1.setDate(200);                               // Chronologically 2nd
        b2 = new RecipeBuilder("user", "recipeTwo");   // Alphabetically 4th
        b2.setDate(400);                               // Chronologically 4th
        b3 = new RecipeBuilder("user", "recipeThree"); // Alphabetically 3rd
        b3.setDate(50);                                // Chronologically 1st
        b4 = new RecipeBuilder("user", "recipeFour");  // Alphabetically 1st
        b4.setDate(300);                               // Chronologically 3rd
        // Use the recipe builders to build the recipes
        r1 = b1.buildRecipe();
        r2 = b2.buildRecipe();
        r3 = b3.buildRecipe();
        r4 = b4.buildRecipe();
        // Add the recipes to the recipe list in the order r1, r2, r3, r4
        recipeList.add(r1);
        recipeList.add(r2);
        recipeList.add(r3);
        recipeList.add(r4);
    }

    /**
     * Sort the recipe list in ascending chronological order
     * Expected order: r3, r1, r4, r2
     */
    @Test
    public void testOldestToNewest() {
        sorter.sortOldestToNewest();
        assertEquals(r3.getDate(), recipeList.get(0).getDate());
        assertEquals(r1.getDate(), recipeList.get(1).getDate());
        assertEquals(r4.getDate(), recipeList.get(2).getDate());
        assertEquals(r2.getDate(), recipeList.get(3).getDate());
    }

    /**
     * Sort the recipe list in descending chronological order
     * Expected order: r2, r4, r1, r3
     */
    @Test
    public void testNewestToOldest() {
        sorter.sortNewestToOldest();
        assertEquals(r2.getDate(), recipeList.get(0).getDate());
        assertEquals(r4.getDate(), recipeList.get(1).getDate());
        assertEquals(r1.getDate(), recipeList.get(2).getDate());
        assertEquals(r3.getDate(), recipeList.get(3).getDate());
    }

    /**
     * Sort the recipe list in ascending alphabetical order
     * Expected order: r4, r1, r3, r2
     */
    @Test
    public void testAToZ() {
        sorter.sortAToZ();
        assertEquals(r4.getTitle(), recipeList.get(0).getTitle());
        assertEquals(r1.getTitle(), recipeList.get(1).getTitle());
        assertEquals(r3.getTitle(), recipeList.get(2).getTitle());
        assertEquals(r2.getTitle(), recipeList.get(3).getTitle());
    }

    /**
     * Sort the recipe list in ascending alphabetical order
     * Expected order: r2, r3, r1, r4
     */
    @Test
    public void testZToA() {
        sorter.sortZToA();
        assertEquals(r2.getTitle(), recipeList.get(0).getTitle());
        assertEquals(r3.getTitle(), recipeList.get(1).getTitle());
        assertEquals(r1.getTitle(), recipeList.get(2).getTitle());
        assertEquals(r4.getTitle(), recipeList.get(3).getTitle());
    }
}