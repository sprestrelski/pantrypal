package code.client.View;

import javafx.scene.layout.*;
import java.io.*;
import java.util.List;

import code.client.Model.AppConfig;
import code.server.Recipe;
import code.client.Model.RecipeListDb;
import code.server.IRecipeDb;
import code.client.Model.RecipeCSVReader;
import code.client.Model.RecipeCSVWriter;

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

    public void setRecipeDB(IRecipeDb recipeDB) {
        this.recipeDb = recipeDB;
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
}