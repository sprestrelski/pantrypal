package code.client.View;

import java.util.ArrayList;

import code.client.Model.Recipe;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DetailsAppFrame implements IWindowUI {
    private ArrayList<IWindowUI> scenes;
    private Stage primaryStage;
    private Scene mainScene;

    private RecipeDetailsUI currentRecipe;
    private RecipeUI recipeUI;
    private Button backToHomeButton;
    VBox root;

    DetailsAppFrame() {
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 11 arial;";
        backToHomeButton = new Button("Back to List");
        backToHomeButton.setStyle(defaultButtonStyle);
        backToHomeButton.setAlignment(Pos.TOP_LEFT);
        addListeners();
    }

    public Scene getSceneWindow() {
        return new Scene(root, 700, 600);
    }

    public void setRecipeHolder(RecipeDetailsUI recipeHolder) {
        currentRecipe = recipeHolder;
    }

    public void setRecipeUI(RecipeUI recipeUI) {
        this.recipeUI = recipeUI;
    }

    /**
     * This method provides the UI holder with the different scenes that can be
     * switched between.
     * 
     * @param primaryStage - Main stage that has the window
     * @param scenes       - list of different scenes to switch between.
     */
    public void setScenes(Stage primaryStage, ArrayList<IWindowUI> scenes) {
        this.scenes = scenes;
        this.primaryStage = primaryStage;
    }

    public void addListeners() {
        backToHomeButton.setOnAction(e -> {
            returnToHome();
        });
    }

    public void returnToHome() {
        // primaryStage.setScene(scenes.get(0).getSceneWindow());
        scenes.get(0).setRoot(mainScene);
    }

    @Override
    public void setRoot(Scene scene) {
        // TODO Auto-generated method stub
        root = new VBox();
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #F0F8FF;");

        VBox setupContainer = new VBox();
        setupContainer.setSpacing(10);

        // Hardcoded value for now, recipe value for it should be changing
        Recipe temp = new Recipe("1", "Fried Chicken and Egg Fried Rice");
        temp.addIngredient("2 chicken breasts, diced");
        temp.addIngredient("2 large eggs");
        temp.addIngredient("2 cups cooked rice");
        temp.addIngredient("2 tablespoons vegetable oil");
        temp.addInstruction("1. Heat the vegetable oil in a large pan over medium-high heat.");

        RecipeDetailsUI details = currentRecipe;// new RecipeDetailsUI(temp);
        recipeUI.setRecipe(details.getRecipe()); // Adds recipe details from chatGPT to the main UI window

        TextField title = details.getTitleField();
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        root.getChildren().addAll(backToHomeButton, title);

        setupContainer.getChildren().add(details);
        root.getChildren().add(setupContainer);

        // Changes the User Screen
        scene.setRoot(root);
    }

    public void setMain(Scene main) {
        mainScene = main;
    }
}
