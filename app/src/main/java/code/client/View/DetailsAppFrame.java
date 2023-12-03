package code.client.View;

import code.client.Model.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.*;

import javafx.event.*;

public class DetailsAppFrame {
    private Recipe currentRecipe;
    private RecipeDetailsUI recipeInfo;
    private Button backToHomeButton, editButton, saveButton, deleteButton, refreshButton, shareButton;
    private VBox detailedUI;
    private String defaultButtonStyle, onStyle, offStyle;

    public DetailsAppFrame() {

        detailedUI = new VBox();
        detailedUI.setSpacing(20);
        detailedUI.setAlignment(Pos.CENTER);
        detailedUI.setStyle("-fx-background-color: #F0F8FF;");
        setupGrowingUI();

        defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 11 arial;";
        onStyle = "-fx-font-style: italic; -fx-background-color: #90EE90; -fx-font-weight: bold; -fx-font: 11 arial;";
        offStyle = "-fx-font-style: italic; -fx-background-color: #FF7377; -fx-font-weight: bold; -fx-font: 11 arial;";

        backToHomeButton = new Button("Back to List");
        backToHomeButton.setStyle(defaultButtonStyle);
        backToHomeButton.setAlignment(Pos.TOP_LEFT);

        refreshButton = new Button("Remake Recipe");
        refreshButton.setStyle(defaultButtonStyle);
        refreshButton.setAlignment(Pos.TOP_RIGHT);

        saveButton = new Button("Save");
        saveButton.setStyle(defaultButtonStyle);
        saveButton.setAlignment(Pos.BOTTOM_CENTER);

        editButton = new Button("Edit");
        editButton.setStyle(offStyle);
        editButton.setAlignment(Pos.BOTTOM_LEFT);

        deleteButton = new Button("Delete");
        deleteButton.setStyle(defaultButtonStyle);
        deleteButton.setAlignment(Pos.BOTTOM_RIGHT);

        shareButton = new Button("Share");
        shareButton.setStyle(defaultButtonStyle);
        shareButton.setAlignment(Pos.BOTTOM_RIGHT);

        // Default recipe
        currentRecipe = getMockedRecipe();
    }

    private void setupGrowingUI() {
        for (int i = 0; i < detailedUI.getChildren().size(); i++) {
            VBox.setVgrow(detailedUI.getChildren().get(i), Priority.ALWAYS);
        }
        VBox.setVgrow(detailedUI, Priority.ALWAYS);
    }

    public Recipe getDisplayedRecipe() {
        String title = recipeInfo.getTitleField().getText();
        String ingredients = recipeInfo.getIngredientsField().getText();
        String instructions = recipeInfo.getInstructionsField().getText();
        String image = recipeInfo.getImageString();

        /// Use Trung's deformatting here.
        String[] ingr = ingredients.split("\n");
        String[] instr = instructions.split("\n");

        Recipe edit = new Recipe(title, currentRecipe.getMealTag());
        for (String ingredient : ingr) {
            edit.addIngredient(ingredient);
        }
        for (String instruction : instr) {
            edit.addInstruction(instruction);
        }
        edit.setImage(image);

        return edit;
    }

    /**
     * This method is used for testing. It's a mock recipe that can be formatted.
     * 
     * @return recipe
     */
    private Recipe getMockedRecipe() {
        // Hardcoded value for now, recipe value for it should be changing
        Recipe temp = new Recipe("Fried Chicken and Egg Fried Rice", "BREAKFAST");
        temp.addIngredient("2 chicken breasts, diced");
        temp.addIngredient("2 large eggs");
        temp.addIngredient("2 cups cooked rice");
        temp.addIngredient("2 tablespoons vegetable oil");
        temp.addInstruction("1. Heat the vegetable oil in a large pan over medium-high heat.");
        return temp;
    }

    public void updateDisplay() {
        // Resets the UI everytime
        detailedUI.getChildren().clear();

        VBox setupContainer = new VBox();
        setupContainer.setSpacing(10);

        recipeInfo = new RecipeDetailsUI(currentRecipe);
        TextField title = recipeInfo.getTitleField();
        title.setAlignment(Pos.TOP_CENTER);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");

        HBox topButtons = new HBox();
        topButtons.setSpacing(100);
        topButtons.setAlignment(Pos.CENTER);
        topButtons.getChildren().addAll(backToHomeButton, refreshButton);
        HBox.setHgrow(topButtons, Priority.ALWAYS);

        detailedUI.getChildren().addAll(topButtons, title);
        setupContainer.getChildren().add(recipeInfo);
        detailedUI.getChildren().add(setupContainer);

        HBox botButtons = new HBox();
        botButtons.setSpacing(100);
        botButtons.setAlignment(Pos.CENTER);
        botButtons.getChildren().addAll(editButton, deleteButton, saveButton, shareButton);

        detailedUI.getChildren().add(botButtons);
    }

    public void setPostButtonAction(EventHandler<ActionEvent> eventHandler) {
        saveButton.setOnAction(eventHandler);
    }

    public void setDeleteButtonAction(EventHandler<ActionEvent> eventHandler) {
        deleteButton.setOnAction(eventHandler);
    }

    public void setHomeButtonAction(EventHandler<ActionEvent> eventHandler) {
        backToHomeButton.setOnAction(eventHandler);
    }

    public void setEditButtonAction(EventHandler<ActionEvent> eventHandler) {
        editButton.setOnAction(eventHandler);
    }

    public void setShareButtonAction(EventHandler<ActionEvent> eventHandler) {
        shareButton.setOnAction(eventHandler);
    }

    public void setRefreshButtonAction(EventHandler<ActionEvent> eventHandler) {
        refreshButton.setOnAction(eventHandler);
    }

    public VBox getRoot(Recipe recipe, boolean old) {
        currentRecipe = recipe;
        if (!old) {
            deleteButton.setVisible(false);
        } else {
            deleteButton.setVisible(true);
        }
        updateDisplay();
        return detailedUI;
    }

    public RecipeDetailsUI getRecipeDetailsUI() {
        return recipeInfo;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getEditButton() {
        return editButton;
    }

}
