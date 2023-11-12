package code.client.View;

import java.util.ArrayList;

import code.client.Model.Recipe;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DetailsAppFrame implements IWindowUI {
    private ArrayList<IWindowUI> scenes;
    private Scene mainScene;

    private RecipeDetailsUI currentRecipe;
    private RecipeUI recipeUI;
    private RecipeList list;
    private Button backToHomeButton, editButton, saveButton, deleteButton;
    VBox detailedUI;
    private boolean editable = false;
    private String defaultButtonStyle, onStyle, offStyle;

    DetailsAppFrame() {

        detailedUI = new VBox();
        detailedUI.setSpacing(20);
        detailedUI.setAlignment(Pos.CENTER);
        detailedUI.setStyle("-fx-background-color: #F0F8FF;");

        defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 11 arial;";
        onStyle = "-fx-font-style: italic; -fx-background-color: #90EE90; -fx-font-weight: bold; -fx-font: 11 arial;";
        offStyle = "-fx-font-style: italic; -fx-background-color: #FF7377; -fx-font-weight: bold; -fx-font: 11 arial;";

        backToHomeButton = new Button("Back to List");
        backToHomeButton.setStyle(defaultButtonStyle);
        backToHomeButton.setAlignment(Pos.TOP_LEFT);

        saveButton = new Button("Save");
        saveButton.setStyle(defaultButtonStyle);
        saveButton.setAlignment(Pos.BOTTOM_CENTER);

        editButton = new Button("Edit");
        editButton.setStyle(offStyle);
        editButton.setAlignment(Pos.BOTTOM_LEFT);

        deleteButton = new Button("Delete");
        deleteButton.setStyle(defaultButtonStyle);
        deleteButton.setAlignment(Pos.BOTTOM_RIGHT);

        // Default recipe
        currentRecipe = getMockedRecipe();

        addListeners();
    }

    /**
     * This method is used to modify what recipe is shown when showing this UI
     * display.
     * 
     * @param recipeHolder - A given recipe in a formatted manner.
     */
    public void setRecipeHolder(RecipeDetailsUI recipeHolder) {
        currentRecipe = recipeHolder;
    }

    /**
     * This method is used to store the provided recipe and its title
     * in the actual list in the home screen.
     * 
     * @param recipeUI - the UI element that stores the recipe name in the Home
     *                 Recipe List
     */
    public void storeNewRecipeUI(RecipeList list, RecipeUI recipeUI) {
        this.recipeUI = recipeUI;
        this.list = list;
    }

    /**
     * This method provides the UI holder with the different scenes that can be
     * switched between.
     * 
     * @param scenes - list of different scenes to switch between.
     */
    public void setScenes(ArrayList<IWindowUI> scenes) {
        this.scenes = scenes;
    }

    public void addListeners() {

        backToHomeButton.setOnAction(e -> {
            returnToHome();
        });
        saveButton.setOnAction(e -> {
            // Takes the values of the provided recipe and applying it to the updated
            // textfield recipes.
            saveButton.setStyle("-fx-background-color: #00FFFF; -fx-border-width: 0;");
            Recipe providedRecipe = currentRecipe.getRecipe();
            Recipe current = new Recipe("0", currentRecipe.getTitleField().getText());
            current.setAllIngredients(providedRecipe.getAllIngredients());
            current.setAllInstructions(providedRecipe.getAllInstructions());

            recipeUI.setRecipe(current);
            list.saveRecipes();

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(f -> saveButton.setStyle(defaultButtonStyle));
            pause.play();
            // System.out.println("Saved: " + currentRecipe.getTitleField().getText());
        });

        editButton.setOnAction(e -> {
            editable = !editable;
            currentRecipe.setEditable(editable);
            if (editable) {
                editButton.setStyle(onStyle);
            } else {
                editButton.setStyle(offStyle);
            }
            displayUpdate(currentRecipe);
            // System.out.println("Edit on");
        });

        deleteButton.setOnAction(e -> {
            deleteRecipe();
            returnToHome();
        });
    }

    public void deleteRecipe() {
        list.getChildren().remove(recipeUI);
        list.saveRecipes();
    }
    public void returnToHome() {
        HomeScreen home = (HomeScreen) scenes.get(0);
        home.setRoot(mainScene);
    }

    /**
     * This method is used for testing. It's a mock recipe that can be formatted.
     * 
     * @return recipe
     */
    private RecipeDetailsUI getMockedRecipe() {
        // Hardcoded value for now, recipe value for it should be changing
        Recipe temp = new Recipe("1", "Fried Chicken and Egg Fried Rice");
        temp.addIngredient("2 chicken breasts, diced");
        temp.addIngredient("2 large eggs");
        temp.addIngredient("2 cups cooked rice");
        temp.addIngredient("2 tablespoons vegetable oil");
        temp.addInstruction("1. Heat the vegetable oil in a large pan over medium-high heat.");
        return new RecipeDetailsUI(temp);
    }

    public void displayUpdate(RecipeDetailsUI details) {
        recipeUI.setRecipe(details.getRecipe()); // Adds recipe details from chatGPT to the main UI window
        // Resets the UI everytime
        detailedUI.getChildren().clear();

        VBox setupContainer = new VBox();
        setupContainer.setSpacing(10);
        TextField title = details.getTitleField();
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        detailedUI.getChildren().addAll(backToHomeButton, title);

        setupContainer.getChildren().add(details);
        detailedUI.getChildren().addAll(setupContainer, saveButton, editButton,deleteButton);
    }

    @Override
    public void setRoot(Scene scene) {

        // used for testing
        currentRecipe = getMockedRecipe();
        RecipeDetailsUI details = getMockedRecipe();
        // used for testing

        // Actual code
        // RecipeDetailsUI details = currentRecipe;

        displayUpdate(details);
        // Changes the User Screen
        scene.setRoot(detailedUI);
        mainScene = scene;
    }
}
