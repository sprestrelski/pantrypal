package code.client.View;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import code.client.Model.Recipe;

public class RecipeUI extends HBox {
    private Label recipeIndex;
    private Button deleteButton, detailsButton;
    private Recipe recipe;

    RecipeUI() {
        // Index of the recipe in the recipe list
        recipeIndex = new Label();
        recipeIndex.setPrefSize(30, 100);
        recipeIndex.setAlignment(Pos.CENTER);
        recipeIndex.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        // Button to enter detailed recipe display
        detailsButton = new Button();
        detailsButton.setPrefSize(570, 100);
        detailsButton.setAlignment(Pos.CENTER_LEFT);
        detailsButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        // Button to delete unwanted recipes from the recipe list
        deleteButton = new Button("Delete");
        deleteButton.setPrefSize(100, 100);
        deleteButton.setAlignment(Pos.CENTER);
        deleteButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        // Add all the elements to the recipe UI
        this.getChildren().addAll(recipeIndex, detailsButton, deleteButton);
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        this.detailsButton.setText(recipe.getTitle());
    }

    public int getRecipeIndex() {
        return Integer.parseInt(this.recipeIndex.getText());
    }

    public void setRecipeIndex(int num) {
        this.recipeIndex.setText(Integer.toString(num));
    }

    public Button getDetailsButton() {
        return this.detailsButton;
    }

    public Button getDeleteButton() {
        return this.deleteButton;
    }

    public String getRecipeName() {
        return this.recipe.getTitle();
    }

}
