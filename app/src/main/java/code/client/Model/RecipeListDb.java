package code.client.Model;

import org.bson.types.ObjectId;

import java.util.List;
import java.util.ArrayList;

public class RecipeListDb implements IRecipeDb {
    private final List<Recipe> recipeList = new ArrayList<>();

    @Override
    public boolean add(Recipe recipe) {
        if (find(recipe.getId()) != null) {
            return false;
        }

        recipeList.add(recipe);
        return true;
    }

    @Override
    public Recipe find(ObjectId id) {
        for (Recipe recipe : recipeList) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public boolean update(Recipe updatedRecipe) {
        ObjectId id = updatedRecipe.getId();
        Recipe recipe = find(id);

        if (recipe == null) {
            return false;
        }

        remove(id);
        add(updatedRecipe);
        return true;
    }

    @Override
    public Recipe remove(ObjectId id) {
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
}
