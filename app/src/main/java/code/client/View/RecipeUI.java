package code.client.View;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import code.client.Model.Recipe;

public class RecipeUI extends HBox {
    private Button mealType;
    private Button deleteButton, detailsButton;
    private Recipe recipe;

    RecipeUI(Recipe recipe) {
        // Index of the recipe in the recipe list
        mealType = new Button();
        mealType.setDisable(true);
        mealType.setPrefSize(250, 50);
        mealType.setAlignment(Pos.CENTER);
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

        this.recipe = recipe;
        this.detailsButton.setText(recipe.getTitle());

        // Add all the elements to the recipe UI
        styleTags(mealType);
        this.getChildren().addAll(mealType, detailsButton, deleteButton);
    }

    private void styleTags(Button mealType) {
        switch (recipe.getMealTag().toLowerCase()) {
            case "breakfast":
                mealType.setStyle("-fx-background-color: #89CFF0; -fx-border-width: 0;");
                mealType.setText("Breakfast");
                break;

            case "lunch":
                mealType.setStyle("-fx-background-color: #00FFFF; -fx-border-width: 0;");
                mealType.setText("Lunch");
                break;

            case "dinner":
                mealType.setStyle("-fx-background-color: #00FF00; -fx-border-width: 0;");
                mealType.setText("Dinner");
                break;
        }
    }

    public Recipe getRecipe() {
        return this.recipe;
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
