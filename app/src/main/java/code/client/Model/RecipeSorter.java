package code.client.Model;

import java.util.List;

import code.server.Recipe;

import java.util.ArrayList;
import java.util.Collections;

public class RecipeSorter {
    private List<Recipe> recipeList;

    public RecipeSorter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }
    
    /*
     * Sort a recipe list from newest to oldest (descending date)
     */
    public void sortNewestToOldest() {
        // First sort the recipe list in ascending chronological order
        sortOldestToNewest();
        // Make a reversed copy of the list in ascending order
        List<Recipe> temp = new ArrayList<>();
        for (int i = recipeList.size() - 1; i >= 0; i--) {
            temp.add(recipeList.get(i));
        }
        // Set the recipe list to match the reversed copy
        for (int i = 0; i < temp.size(); i++) {
            recipeList.set(i, temp.get(i));
        }
    }
    
    /*
     * Sort a recipe list from oldest to newest (ascending date)
     */
    public void sortOldestToNewest() {
        Collections.sort(recipeList, Recipe.Comparators.DATE);
    }

    /*
     * Sort a recipe list in ascending alphabetical order
     */
    public void sortAToZ() {
        Collections.sort(recipeList, Recipe.Comparators.TITLE); 
    }

    /*
     * Sort a recipe list in descending alphabetical order
     */
    public void sortZToA() {
        // First sort the recipe list in ascending alphabetical order
        sortAToZ();
        // Make a reversed copy of the list in ascending order
        List<Recipe> temp = new ArrayList<>();
        for (int i = recipeList.size() - 1; i >= 0; i--) {
            temp.add(recipeList.get(i));
        }
        // Set the recipe list to match the reversed copy
        for (int i = 0; i < temp.size(); i++) {
            recipeList.set(i, temp.get(i));
        }
    }
}
