package code.client.View;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import java.io.*;
import java.util.List;

import code.server.Recipe;
import code.server.IRecipeDb;

public class RecipeListUI extends VBox {
    private IRecipeDb recipeDb;

    RecipeListUI() throws IOException {
        this.setSpacing(5);
        this.setPrefSize(700, 500);
        this.setStyle("-fx-background-color: #F0F8FF;");
        //VBox.setVgrow(this, Priority.ALWAYS);
        this.setAlignment(Pos.CENTER);
    }

    public IRecipeDb getRecipeDB() {
        return this.recipeDb;
    }

    public void setRecipeDB(IRecipeDb recipeDB) {
        this.recipeDb = recipeDB;
    }

    public void update(String filter) {
        getChildren().clear();
        List<Recipe> recipeList = recipeDb.getList();
        RecipeUI recipeUI;
        for (Recipe recipe : recipeList) {
            if (filter.equals("none") || recipe.getMealTag().toLowerCase().equals(filter.toLowerCase())) {
                recipeUI = new RecipeUI(recipe);
                this.getChildren().add(recipeUI);
            }
        }
    }
}