
package code.client.View;

import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.io.*;
import java.lang.StackWalker.StackFrame;

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

    private Button filterButton, sortButton;

    Header() {
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 13 arial;";

        filterButton = new Button("Filter");
        filterButton.setStyle(defaultButtonStyle);

        sortButton = new Button("Sort");
        sortButton.setStyle(defaultButtonStyle);

        this.setPrefSize(700, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Recipe List");
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().addAll(filterButton, titleText, sortButton);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(200);
    }
}

public class AppFrameHome extends BorderPane {
    private Header header;
    private Footer footer;
    private RecipeListUI recipeList;
    private Button newButton;
    private Scene mainScene;
    private StackPane stack;

    AppFrameHome() throws IOException {
        stack = new StackPane();

        header = new Header();
        recipeList = new RecipeListUI();
        footer = new Footer();
        recipeList.loadRecipes();
        ScrollPane scroller = new ScrollPane(recipeList);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);

        this.setTop(header);
        this.setCenter(scroller);
        this.setBottom(footer);

        newButton = footer.getNewButton();
    }

    public StackPane getRoot() {
        stack.getChildren().clear();
        stack.getChildren().add(this);
        return stack;
    }

    public void setMain(Scene main) {
        mainScene = main;
    }

    public void setNewRecipeButtonAction(EventHandler<ActionEvent> eventHandler) {
        newButton.setOnAction(eventHandler);
    }

    // public void setRecipeDetailsButtonAction(EventHandler<ActionEvent>
    // eventHandler) {
    // for (int i = 0; i < recipeList.getChildren().size(); i++) {
    // RecipeUI currRecipe = (RecipeUI) recipeList.getChildren().get(i);
    // currRecipe.getDetailsButton().setOnAction(eventHandler);
    // // currRecipe.getDeleteButton().setOnAction(eventHandler);
    // }
    // }

    public Button getNewButton() {
        return footer.getNewButton();
    }

    public RecipeListUI getRecipeList() {
        return recipeList;
    }

}
