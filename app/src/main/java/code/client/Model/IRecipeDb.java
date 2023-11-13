package code.client.Model;

public interface IRecipeDb extends Iterable<Recipe> {
    void add(Recipe recipe);
    void remove(Recipe recipe);
    void clear();
}