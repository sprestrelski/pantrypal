
package code.client.View;

import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.io.*;

import javafx.geometry.Pos;
import java.util.ArrayList;
import java.util.UUID;

import code.client.Model.*;
import code.client.View.*;
import code.client.Controllers.*;
import javafx.event.*;

class Footer extends HBox {
    // Button for creating a new recipe
    private Button newButton;

    Footer() {

        this.setPrefSize(700, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 11 arial;";

        newButton = new Button("New Recipe");
        newButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(newButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getNewButton() {
        return this.newButton;
    }
}

class Header extends HBox {

    Header() {
        this.setPrefSize(700, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Recipe List");
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER);
    }
}

public class AppFrameHome extends BorderPane {
    private Header header;
    private Footer footer;
    private RecipeListUI recipeList;
    private Button deleteButton, detailsButton, newButton;
    private ArrayList<IWindowUI> scenes;
    private Scene mainScene;

    AppFrameHome() throws IOException {
        header = new Header();
        recipeList = new RecipeListUI();
        updateDisplay();
        footer = new Footer();
        recipeList.loadRecipes();
        ScrollPane scroller = new ScrollPane(recipeList);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);

        this.setTop(header);
        this.setCenter(scroller);
        this.setBottom(footer);

        newButton = footer.getNewButton();

        addListeners(null);
    }

    public Scene getSceneWindow() {
        return new Scene(this, 700, 600);
    }

    public void addListeners(RecipeUI savedRecipe) {
        if (savedRecipe != null) {
            addListenersInRecipe(savedRecipe);
            return;
        }
        newButton.setOnAction(create -> {
            RecipeUI recipe = new RecipeUI();
            // recipeList.getChildren().add(0, recipe);

            addListenersInRecipe(recipe);

            recipeList.updateRecipeIndices();
            NewRecipeUI audioPrompt;
            /*
             * Create a new audio prompting window each time.
             */
            try {
                audioPrompt = new NewRecipeUI(); // (NewRecipeUI) scenes.get(1);
                audioPrompt.storeNewRecipeUI(recipeList, recipe);

                scenes.set(1, audioPrompt);
                audioPrompt.setScenes(scenes);

                audioPrompt.setRoot(mainScene);

            } catch (Exception e3) {
                e3.printStackTrace();
            }
        });
    }

    public void addListenersInRecipe(RecipeUI recipe) {
        detailsButton = recipe.getDetailsButton();
        detailsButton.setOnAction(read -> {
            DetailsAppFrame details = (DetailsAppFrame) scenes.get(2);

            details.setRecipeHolder(new RecipeDetailsUI(recipe.getRecipe())); // should have RecipeDetailsUI
            details.storeNewRecipeUI(recipeList, recipe);

            details.setRoot(mainScene); // Changes UI to Detailed Recipe Screen

        });

        deleteButton = recipe.getDeleteButton();
        deleteButton.setOnAction(delete -> {
            recipeList.getRecipeDB().remove(recipe.getRecipe()); // TODO: SERVER DELETE REQUEST
            recipeList.saveRecipes();
            recipeList.update();
        });

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

    public AppFrameHome getRoot() {
        return this;
    }

    public void setMain(Scene main) {
        mainScene = main;
    }

    public void setGetButtonAction(EventHandler<ActionEvent> eventHandler) {
        detailsButton.setOnAction(eventHandler);
    }

    public void updateDisplay() {
        recipeList.update();
        for (int i = 0; i < recipeList.getChildren().size(); i++) {
            RecipeUI currRecipe = (RecipeUI) recipeList.getChildren().get(i);
            addListenersInRecipe(currRecipe);
        }
        this.setCenter(recipeList);
    }

}
