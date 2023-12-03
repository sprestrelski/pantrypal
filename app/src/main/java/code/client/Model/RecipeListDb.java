package code.client.Model;

import java.util.List;

import java.util.ArrayList;
import code.server.Recipe;
import code.server.IRecipeDb;

public class RecipeListDb implements IRecipeDb {
    private List<Recipe> recipeList = new ArrayList<>();

    @Override
    public boolean add(Recipe recipe) {
        if (find(recipe.getId()) != null) {
            return false;
        }

        recipeList.add(recipe);
        return true;
    }

    @Override
    public Recipe find(String id) {
        for (Recipe recipe : recipeList) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public boolean update(Recipe updatedRecipe) {
        String id = updatedRecipe.getId();
        Recipe recipe = find(id);

        if (recipe == null) {
            return false;
        }

        remove(id);
        add(updatedRecipe);
        return true;
    }

    @Override
    public Recipe remove(String id) {
        Recipe recipe;

        for (int i = 0; i < recipeList.size(); i++) {
            recipe = recipeList.get(i);
            if (recipe.getId().equals(id)) {
                recipeList.remove(i);
                return recipe;
            }
        }

        return null;
    }

    @Override
    public void clear() {
        recipeList.clear();
    }

    @Override
    public int size() {
        return recipeList.size();
    }

    @Override
    public List<Recipe> getList() {
        return recipeList;
    }

    @Override
    public List<Recipe> getList(String accountId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getList'");
    }
}
