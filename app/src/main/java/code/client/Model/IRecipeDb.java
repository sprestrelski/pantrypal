package code.client.Model;

import java.util.UUID;

public interface IRecipeDb extends Iterable<Recipe> {
    void add(Recipe recipe);

    void add(int index, Recipe recipe);

    Recipe find(UUID id);

    void set(UUID id, Recipe newRecipe);

    void remove(Recipe recipe);

    void clear();

    int size();
}