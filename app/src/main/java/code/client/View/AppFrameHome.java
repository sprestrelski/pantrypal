
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

import javafx.collections.FXCollections;

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

    // Drop down menu for choosing filtering and sorting criteria
    private MenuButton filterMenuButton, sortMenuButton;
    // Filtering criteria contained in the dropdown menu
    private MenuItem filterBreakfast, filterLunch, filterDinner, filterNone;
    // Sorting crteria contained in the dropdown menu
    private MenuItem sortNewToOld, sortOldToNew, sortAToZ, sortZToA;

    Header() {
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 13 arial;";

        filterMenuButton = new MenuButton("Filter");
        filterMenuButton.setStyle(defaultButtonStyle);

        filterBreakfast = new MenuItem("Breakfast");
        filterLunch = new MenuItem("Lunch");
        filterDinner = new MenuItem("Dinner");
        filterNone = new MenuItem("None");

        filterMenuButton.getItems().addAll(filterBreakfast, filterLunch, filterDinner, filterNone);

        sortMenuButton = new MenuButton("Sort");
        sortMenuButton.setStyle(defaultButtonStyle);

        sortNewToOld = new MenuItem("Sort by date (Newest to Oldest)");
        sortOldToNew = new MenuItem("Sort by date (Oldest to Newest)");
        sortAToZ = new MenuItem("Sort alphabetically (A-Z)");
        sortZToA = new MenuItem("Sort alphabetically (Z-A)");

        sortMenuButton.getItems().addAll(sortNewToOld, sortOldToNew, sortAToZ, sortZToA);

        this.setPrefSize(700, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Recipe List");
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().addAll(filterMenuButton, titleText, sortMenuButton);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(200);
    }

    public MenuButton getSortMenuButton() {
        return this.sortMenuButton;
    }

    public MenuButton getFilterMenuButton() {
        return this.filterMenuButton;
    }
}

public class AppFrameHome extends BorderPane {
    private Header header;
    private Footer footer;
    private RecipeListUI recipeList;
    private Button newButton;
    private MenuButton filterMenuButton, sortMenuButton;
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
        filterMenuButton = header.getFilterMenuButton();
        sortMenuButton = header.getSortMenuButton();
    }

    public StackPane getRoot() {
        stack.getChildren().clear();
        stack.getChildren().add(this);
        this.updateDisplay();
        return stack;
    }

    public void updateDisplay() {
        recipeList.update();
        for (int i = 0; i < recipeList.getChildren().size(); i++) {
            RecipeUI currRecipe = (RecipeUI) recipeList.getChildren().get(i);
        }
        this.setCenter(recipeList);
    }

    public void setMain(Scene main) {
        mainScene = main;
    }

    public void setNewRecipeButtonAction(EventHandler<ActionEvent> eventHandler) {
        newButton.setOnAction(eventHandler);
    }

    public void setFilterMenuButtonAction(EventHandler<ActionEvent> eventHandler) {
        filterMenuButton.setOnAction(eventHandler);
    }

    public void setSortMenuButtonAction(EventHandler<ActionEvent> eventHandler) {
        sortMenuButton.setOnAction(eventHandler);
    }

    public void setRecipeDetailsButtonAction(EventHandler<ActionEvent> eventHandler) {
        for (int i = 0; i < recipeList.getChildren().size(); i++) {
            RecipeUI currRecipe = (RecipeUI) recipeList.getChildren().get(i);
            currRecipe.getDetailsButton().setOnAction(eventHandler);
            currRecipe.getDeleteButton().setOnAction(eventHandler);
        }
    }

    public MenuButton getSortMenuButton() {
        return header.getSortMenuButton();
    }

    public MenuButton getFilterMenuButton() {
        return header.getFilterMenuButton();
    }

    public Button getNewButton() {
        return footer.getNewButton();
    }

    public RecipeListUI getRecipeList() {
        return recipeList;
    }
}
