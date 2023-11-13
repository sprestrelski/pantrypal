package code.client.Model;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class RecipeDb implements IRecipeDb {
    private List<Recipe> recipeList;
    
    public RecipeDb() {
        recipeList = new ArrayList<>();
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void add(Recipe recipe) {
        recipeList.add(recipe);
    }

    public void add(int index, Recipe recipe) {
        recipeList.add(index, recipe);
    }

    public void addAll(ArrayList<Recipe> recipes) {
        recipeList.addAll(recipes);
    }

    public void remove(Recipe recipe) {
        recipeList.remove(recipe);
    }

    public void clear() {
        recipeList.clear();
    }

    @Override
    public Iterator<Recipe> iterator() {
        return recipeList.iterator();
    }
}
