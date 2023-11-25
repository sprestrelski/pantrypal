package code.client.Model;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class RecipeDb implements IRecipeDb {
    private final List<Recipe> recipeList = new ArrayList<>();

    public void add(Recipe recipe) {
        recipeList.add(recipe);
    }

    public void add(int index, Recipe recipe) {
        recipeList.add(index, recipe);
    }

    public void addAll(ArrayList<Recipe> recipes) {
        recipeList.addAll(recipes);
    }

    public Recipe find(UUID id) {
        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).getId() == id) {
                return recipeList.get(i);
            }
        }
        return null;
    }

    public void set(UUID id, Recipe newRecipe) {
        int index = -1;
        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).getId() == id) {
                index = i;
            }
        }
        if (index >= 0) {
            recipeList.set(index, newRecipe);
        }
        recipeList.add(0, newRecipe);

    }

    public void remove(Recipe recipe) {
        recipeList.remove(recipe);
    }

    public Recipe remove(UUID id) {
        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).getId() == id) {
                Recipe temp = recipeList.get(i);
                recipeList.remove(recipeList.get(i));
                return temp;
            }
        }
        return null;
    }

    public void clear() {
        recipeList.clear();
    }

    @Override
    public Iterator<Recipe> iterator() {
        return recipeList.iterator();
    }

    @Override
    public int size() {
        return recipeList.size();
    }
}
