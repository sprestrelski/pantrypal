package code.client.Model;
import java.util.UUID;

public interface IRecipeDb extends Iterable<Recipe> {
    void add(Recipe recipe);
    Recipe find(UUID id);
    void remove(Recipe recipe);
    void clear();
    int size();
}