package code.client.Model;

import java.util.List;
import org.bson.types.ObjectId;

public interface IRecipeDb {
    List<Recipe> getList();
    boolean add(Recipe recipe);
    Recipe find(ObjectId id);
    boolean update(Recipe updatedRecipe);
    Recipe remove(ObjectId id);
    void clear();
    int size();
}