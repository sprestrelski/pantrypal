
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

    private Button filterButton;
    // private Button sortButton;
    // Drop down menu for choosing sorting criteria
    private MenuButton sortMenuButton;
    // Sorting crteria contained in the dropdown menu
    private MenuItem sortNtoO, sortOtoN, sortAtoZ, sortZtoA;

    Header() {
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 13 arial;";

        filterButton = new Button("Filter");
        filterButton.setStyle(defaultButtonStyle);

        // sortButton = new Button("Sort");
        // sortButton.setStyle(defaultButtonStyle);

        sortMenuButton = new MenuButton("Sort");
        sortMenuButton.setStyle(defaultButtonStyle);

        sortNtoO = new MenuItem("Sort by date (Newest to Oldest)");
        sortOtoN = new MenuItem("Sort by date (Oldest to Newest)");
        sortAtoZ = new MenuItem("Sort alphabetically (A-Z)");
        sortZtoA = new MenuItem("Sort alphabetically (Z-A)");

        sortMenuButton.getItems().addAll(sortNtoO, sortOtoN, sortAtoZ, sortZtoA);
        

        // sortNtoOButton = new Button("Alphabetically (A-Z)");
        // sortNtoOButton.setStyle(defaultButtonStyle);
        
        // sortOtoNButton = new Button("Alphabetically (Z-A)");
        // sortOtoNButton.setStyle(defaultButtonStyle);

        // sortChoiceBox = new ChoiceBox<>();
        // sortChoiceBox.setItems(FXCollections.observableArrayList(
        //     "Sort by date (Newest to Oldest)",
        //     "Sort by date  (Oldest to Newest)",
        //     "Sort alphabetically (A-Z)",
        //     "Sort alphabetically (Z-A)"
        // ));
        // sortChoiceBox.setValue("Sort by date (Newest to Oldest)"); //temp (change to user selection later)


        this.setPrefSize(700, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Recipe List");
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().addAll(filterButton, titleText, sortMenuButton); //thjere was sortButton before
        this.setAlignment(Pos.CENTER);
        this.setSpacing(200);
    }

    // public ChoiceBox<String> getSortChoiceBox() {
    //     return this.sortChoiceBox;
    // }

    public MenuButton getSortMenuButton() {
        return this.sortMenuButton;
    }

    public Button getFilterButton() {
        return this.filterButton;
    }

    // public Button getSortButton() {
    //     return this.sortButton;
    // }
}

public class AppFrameHome extends BorderPane {
    private Header header;
    private Footer footer;
    private RecipeListUI recipeList;
    private Button newButton, filterButton, sortButton;
    private MenuButton sortMenuButton;
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
        filterButton = header.getFilterButton();
        sortMenuButton = header.getSortMenuButton();
        
        // sortButton = header.getSortButton();

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

    public void setFilterButtonAction(EventHandler<ActionEvent> eventHandler) {
        filterButton.setOnAction(eventHandler);
    }

    // public void setSortButtonAction(EventHandler<ActionEvent> eventHandler) {
    //     sortButton.setOnAction(eventHandler);
    // }

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

    // public Button getSortButton() {
    //     return header.getSortButton();
    // }

    public Button getFilterButton() {
        return header.getFilterButton();
    }

    public Button getNewButton() {
        return footer.getNewButton();
    }

    public RecipeListUI getRecipeList() {
        return recipeList;
    }
}
