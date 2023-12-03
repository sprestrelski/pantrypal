package code.client.Model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class RecipeSorter {
    private List<Recipe> recipeList;

    public RecipeSorter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }
    
    /*
     * Sort a recipe list from newest to oldest recipes
     */
    public void sortNewestToOldest() {
        Collections.sort(recipeList, Recipe.Comparators.DATE);
    }
    
    /*
     * Sort a recipe list from oldest to newest recipes
     */
    public void sortOldestToNewest() {
        // First sort the recipe list in newest to oldest order
        sortNewestToOldest();
        // Copy the sorted list to a temp list in reverse order
        List<Recipe> temp = new ArrayList<>();
        for (int i = recipeList.size() - 1; i >= 0; i--) {
            temp.add(recipeList.get(i));
        }
        // Set the sorted list to be in reverse order
        for (int i = 0; i < temp.size(); i++) {
            recipeList.set(i, temp.get(i));
        }
    }

    /*
     * Alphabetically sort a recipe list in ascending order
     */
    public void sortAToZ() {
        Collections.sort(recipeList, Recipe.Comparators.TITLE); 
    }

    /*
     * Alphabetically sort a recipe list in descending order
     */
    public void sortZToA() {
        // First sort the recipe list in alphabetical order
        sortAToZ();
        // Copy the sorted list to a temp list in reverse order
        List<Recipe> temp = new ArrayList<>();
        for (int i = recipeList.size() - 1; i >= 0; i--) {
            temp.add(recipeList.get(i));
        }
        // Set the sorted list to be in reverse order
        for (int i = 0; i < temp.size(); i++) {
            recipeList.set(i, temp.get(i));
        }
    }
    
    // public void sortAToZ() {
    //     // Create a list to store a copy of the recipe list
    //     List<Recipe> copies = new ArrayList<>();
    //     // Create a recipe object that copies over all of the information from a recipe
    //     Recipe copy;
    //     Recipe curr;
    //     // Iterate through all of the recipes to make a copy of the unsorted recipe list
    //     for (int i = 0; i < this.recipeList.size(); i++) {
    //         curr = this.recipeList.get(i);
    //         copy = new Recipe(curr.getId(), curr.getTitle()); // Make a new recipe to copy over the information from the current recipe
    //         copies.add(copy);
    //     }
    //     Collections.sort(copies, new Comparator<Recipe>() {
    //         @Override
    //         public int compare(Recipe r1, Recipe r2) {
    //             return r1.getTitle().compareTo(r2.getTitle());
    //         }
    //     });
    //     // Iterate through all of the recipes to put each at its sorted index
    //     for (int i = 0; i < this.recipeList.size(); i++) {
    //         this.recipeList.set(i, copies.get(i));
    //     }
    // }

    // public void sortZToA() {
    //     sortAToZ();
    //     List<Recipe> temp = new ArrayList<>();
    //     for (int i = this.recipeList.size() - 1; i >= 0; i--) {
    //         temp.add(this.recipeList.get(i));
    //     }
    //     for (int i = 0; i < temp.size(); i++) {
    //         this.recipeList.set(i, temp.get(i));
    //     }
    // }
}
