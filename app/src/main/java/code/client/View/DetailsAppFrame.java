package code.client.View;

import java.util.ArrayList;

import code.client.Model.Recipe;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DetailsAppFrame {
    private ArrayList<Scene> scenes;
    private Stage primaryStage;

    public Scene getScene() {
        VBox root = new VBox();
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

        RecipeDetailsUI details = new RecipeDetailsUI(temp);

        TextField title = details.getTitleField();
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        root.getChildren().add(title);

        setupContainer.getChildren().add(details);
        root.getChildren().add(setupContainer);

        return new Scene(root, 700, 600);
    }

    /**
     * This method provides the UI holder with the different scenes that can be
     * switched between.
     * 
     * @param primaryStage - Main stage that has the window
     * @param scenes       - list of different scenes to switch between.
     */
    public void setScenes(Stage primaryStage, ArrayList<Scene> scenes) {
        this.scenes = scenes;
        this.primaryStage = primaryStage;
    }
}
