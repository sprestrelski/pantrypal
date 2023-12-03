package code.server;

import java.util.List;

public interface IRecipeDb {
    List<Recipe> getList(String accountId);

    List<Recipe> getList();

    boolean add(Recipe recipe);

    Recipe find(String id);

    boolean update(Recipe updatedRecipe);

    Recipe remove(String id);

    void clear();

    int size();
}