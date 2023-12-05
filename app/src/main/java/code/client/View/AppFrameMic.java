package code.client.View;

import code.client.Model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import java.io.IOException;
import java.net.URISyntaxException;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

class GPTRecipe extends GridPane {
    private Label recipeLabel;
    private TextField recipeField;

    GPTRecipe() {
        this.setVgap(20);
        recipeLabel = new Label("Here Is Your Recipe");
        recipeField = new TextField();
        recipeField.setPrefWidth(500); // change
        recipeField.setPrefHeight(200); // change
        recipeLabel.setStyle("-fx-font-size: 16"); // change
        recipeLabel.setTextFill(Color.web("#FF0000")); // change
        this.add(recipeLabel, 0, 0);
        this.add(recipeField, 0, 1);
    }
}

class HeaderMic extends HBox {
    HeaderMic() {
        this.setPrefSize(700, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Recipe Creation");
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER);
    }
}

public class AppFrameMic extends BorderPane {
    // Helper variables for button functionality
    // AppFrameMic elements
    private GridPane recipeCreationGrid;
    private HeaderMic header;
    private MealType mealTypeSelection;
    private Ingredients ingredientsList;
    private Button recordMealTypeButton, recordIngredientsButton, goToDetailedButton, backButton;
    private Label recordingMealTypeLabel, recordingIngredientsLabel;

    AppFrameMic() throws URISyntaxException, IOException {
        setStyle("-fx-background-color: #DAE5EA;"); // If want to change
        // background color
        header = new HeaderMic();
        mealTypeSelection = new MealType();
        ingredientsList = new Ingredients();

        recipeCreationGrid = new GridPane();
        recipeCreationGrid.setAlignment(Pos.CENTER);
        recipeCreationGrid.setVgap(5);
        recipeCreationGrid.setHgap(5);
        recipeCreationGrid.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        recipeCreationGrid.add(mealTypeSelection, 0, 1);
        recipeCreationGrid.add(ingredientsList, 0, 2);

        this.setTop(header);
        this.setCenter(recipeCreationGrid);

        recordMealTypeButton = mealTypeSelection.getRecordButton();
        recordingMealTypeLabel = mealTypeSelection.getRecordingLabel();
        recordIngredientsButton = ingredientsList.getRecordButton();
        recordingIngredientsLabel = ingredientsList.getRecordingLabel();

        goToDetailedButton = new Button("See Detailed Recipe");
        recipeCreationGrid.add(goToDetailedButton, 0, 5);

        backButton = new Button("Back to List");
        recipeCreationGrid.add(backButton, 0, 0);
    }

    public void setGoToDetailedButtonAction(EventHandler<ActionEvent> eventHandler) {
        goToDetailedButton.setOnAction(eventHandler);
    }

    public void setGoToHomeButtonAction(EventHandler<ActionEvent> eventHandler) {
        backButton.setOnAction(eventHandler);
    }

    public void setRecordMealTypeButtonAction(EventHandler<ActionEvent> eventHandler) {
        recordMealTypeButton.setOnAction(eventHandler);
    }

    public void setRecordIngredientsButtonAction(EventHandler<ActionEvent> eventHandler) {
        recordIngredientsButton.setOnAction(eventHandler);
    }

    public Button getRecordMealTypeButton() {
        return recordMealTypeButton;
    }

    public Button getRecordIngredientsButton() {
        return recordIngredientsButton;
    }

    public Label getMealTypeLabel() {
        return recordingMealTypeLabel;
    }

    public Label getRecordingIngredientsLabel() {
        return recordingIngredientsLabel;
    }

    public Label getRecordingMealTypeLabel() {
        return recordingMealTypeLabel;
    }

    public MealType getMealBox() {
        return mealTypeSelection;
    }

    public Ingredients getIngredBox() {
        return ingredientsList;
    }

    public StackPane getRoot() {
        StackPane stack = new StackPane();
        stack.getChildren().add(this);
        return stack;
    }
}
