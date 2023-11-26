package code.client.View;

import javafx.scene.layout.*;
import java.io.*;
import java.util.List;

import code.client.Model.IRecipeDb;
import code.client.Model.Recipe;
import code.client.Model.RecipeListDb;
import code.client.Model.RecipeCSVReader;
import code.client.Model.RecipeCSVWriter;

// TODO: SERVER Controller that sends a GET(db) request for the recipeDB.
public class RecipeListUI extends VBox {
    private static final String CSV_FILE = "recipes.csv";
    private IRecipeDb recipeDb = new RecipeListDb();
    private RecipeCSVWriter recipeWriter;

    RecipeListUI() throws IOException {
        this.setSpacing(5);
        this.setPrefSize(700, 600);
        this.setStyle("-fx-background-color: #F0F8FF;");
        loadRecipes();
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    /*
     * Update the indices of the recipes in the list whenever a new recipe is added
     * or removed
     */
    public void updateRecipeIndices() {
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof RecipeUI) {
                ((RecipeUI) this.getChildren().get(i)).setRecipeIndex(i + 1);
            }
        }
    }

    /*
     * Remove a recipe from the recipe list whenever the delete button is clicked
     */
    public void removeRecipe(int index) {
        this.getChildren().remove(index - 1);
        this.updateRecipeIndices();
    }

    /*
     * Save recipes to a file called "recipes.csv"
     */
    public void saveRecipes() {
        try {
            Writer writer = new BufferedWriter(new FileWriter(CSV_FILE));
            recipeWriter = new RecipeCSVWriter(writer);
            recipeWriter.writeRecipeDb(recipeDb);
            writer.close();
        } catch (IOException e) {
            System.out.println("Recipes could not be saved.");
            e.printStackTrace();
        }
    }

    public IRecipeDb getRecipeDB() {
        return this.recipeDb;
    }

    /* Maybe in Controller */
    /**
     * This method updates the RecipeListUI to display all the recipes stored in the
     * database.
     */
    public void update() {
        // RecipeListUI = this
        this.getChildren().clear();
        List<Recipe> recipeList = recipeDb.getList();

        for (Recipe recipe : recipeList) {
            RecipeUI temp = new RecipeUI();
            temp.setRecipe(recipe);
            this.getChildren().add(temp);
        }
    }

    /*public void update(ArrayList<Filters> filters) {
        // RecipeListUI = this
        this.getChildren().clear();
        for (Recipe recipe : recipeDb) {
            if(filters.contains(recipe.getMealType())) {
                RecipeUI temp = new RecipeUI();
                temp.setRecipe(recipe);
                this.getChildren().add(temp);
            }
            else {}
        }
    }*/

    /*
     * Load recipes from a file called "recipes.csv" to RecipeDb
     */
    public void loadRecipes() {
        try {
            Reader reader = new FileReader(CSV_FILE);
            RecipeCSVReader recipeReader = new RecipeCSVReader(reader);
            IRecipeDb recipeDb = new RecipeListDb();
            recipeReader.readRecipeDb(recipeDb);
            System.out.println("Recipes loaded");
        } catch (IOException e) {
            System.out.println("Recipes could not be loaded.");
        }
    }
}