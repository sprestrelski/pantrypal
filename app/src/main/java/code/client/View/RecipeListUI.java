package code.client.View;

import javafx.scene.layout.*;
import java.io.*;
import java.util.List;

import code.client.Model.AppConfig;
import code.client.Model.IRecipeDb;
import code.client.Model.Recipe;
import code.client.Model.RecipeListDb;
import code.client.Model.RecipeCSVReader;
import code.client.Model.RecipeCSVWriter;

// TODO: SERVER Controller that sends a GET(db) request for the recipeDB.
public class RecipeListUI extends VBox {
    private IRecipeDb recipeDb;

    RecipeListUI() throws IOException {
        this.setSpacing(5);
        this.setPrefSize(700, 600);
        this.setStyle("-fx-background-color: #F0F8FF;");
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    public IRecipeDb getRecipeDB() {
        return this.recipeDb;
    }


    public void update() {
        getChildren().clear();
        List<Recipe> recipeList = recipeDb.getList();
        RecipeUI recipeUI;

        for (Recipe recipe : recipeList) {
            recipeUI = new RecipeUI(recipe);
            this.getChildren().add(recipeUI);
        }
    }
    
    /*
     * TODO : ERADICATE THIS from here
     * Load recipes from a file called "recipes.csv" to RecipeDb
     */
    public void loadRecipes() {
        try {
            Reader reader = new FileReader(AppConfig.CSV_FILE);
            RecipeCSVReader recipeReader = new RecipeCSVReader(reader);
            recipeDb = new RecipeListDb();
            recipeReader.readRecipeDb(recipeDb);
            System.out.println("Recipes loaded");
        } catch (IOException e) {
            System.out.println("Recipes could not be loaded.");
        }
    }
}