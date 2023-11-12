package code.client.View;

import java.util.ArrayList;

import code.client.Model.Recipe;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DetailsAppFrame implements IWindowUI {
    private ArrayList<IWindowUI> scenes;
    private Scene mainScene;

    private RecipeDetailsUI currentRecipe;
    private RecipeUI recipeUI;
    private RecipeList list;
    private Button backToHomeButton, saveButton;
    VBox detailedUI;

    DetailsAppFrame() {

        detailedUI = new VBox();
        detailedUI.setSpacing(20);
        detailedUI.setAlignment(Pos.CENTER);
        detailedUI.setStyle("-fx-background-color: #F0F8FF;");

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 11 arial;";
        backToHomeButton = new Button("Back to List");
        backToHomeButton.setStyle(defaultButtonStyle);
        backToHomeButton.setAlignment(Pos.TOP_LEFT);

        saveButton = new Button("Save");
        saveButton.setStyle(defaultButtonStyle);
        saveButton.setAlignment(Pos.BOTTOM_CENTER);

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
     * @param primaryStage - Main stage that has the window
     * @param scenes       - list of different scenes to switch between.
     */
    public void setScenes(ArrayList<IWindowUI> scenes) {
        this.scenes = scenes;
    }

    public void addListeners() {
        backToHomeButton.setOnAction(e -> {
            returnToHome();
        });
        saveButton.setOnAction(e -> {
            Recipe curr = currentRecipe.getRecipe();
            // recipeUI.setRecipeIngredients(curr.getAllIngredients());
            // recipeUI.setRecipeInstructions(curr.getAllIngredients());
            list.saveRecipes();
        });
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

    @Override
    public void setRoot(Scene scene) {
        // Resets the UI everytime
        detailedUI.getChildren().clear();

        VBox setupContainer = new VBox();
        setupContainer.setSpacing(10);

        RecipeDetailsUI details = currentRecipe;// getMockedRecipe();
        recipeUI.setRecipe(details.getRecipe()); // Adds recipe details from chatGPT to the main UI window

        TextField title = details.getTitleField();
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        detailedUI.getChildren().addAll(backToHomeButton, title);

        setupContainer.getChildren().add(details);
        detailedUI.getChildren().addAll(setupContainer, saveButton);

        // Changes the User Screen
        scene.setRoot(detailedUI);
        mainScene = scene;
    }
}
