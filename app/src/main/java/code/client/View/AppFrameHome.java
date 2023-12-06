
package code.client.View;

import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.io.*;
import javafx.geometry.Pos;
import javafx.event.*;

class Footer extends HBox {
    // Button for creating a new recipe
    private Button newButton, logOutButton;

    Footer() {
        GridPane grid = new GridPane();
        this.setPrefSize(620, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 11 arial;";

        newButton = new Button("New Recipe");
        newButton.setStyle(defaultButtonStyle);

        logOutButton = new Button("Log out");
        logOutButton.setStyle(defaultButtonStyle);
        grid.add(logOutButton, 3, 0);
        grid.add(newButton, 11, 0);
        grid.setHgap(20);
        this.getChildren().add(grid);
        this.setAlignment(Pos.CENTER);
    }

    public Button getNewButton() {
        return this.newButton;
    }

    public Button getLogOutButton() {
        return this.logOutButton;
    }
}

class Header extends HBox {

    // Drop down menu for choosing filtering and sorting criteria
    private MenuButton filterMenuButton, sortMenuButton;
    // Filtering criteria contained in the dropdown menu
    private MenuItem filterBreakfast, filterLunch, filterDinner, filterNone;
    // Sorting criteria contained in the dropdown menu
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

        this.setPrefSize(620, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Recipe List");
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().addAll(filterMenuButton, titleText, sortMenuButton);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(150);
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
    private Button newButton, logOutButton;

    AppFrameHome() throws IOException {

        header = new Header();
        recipeList = new RecipeListUI();
        footer = new Footer();
        ScrollPane scroller = new ScrollPane(recipeList);
        scroller.setMaxSize(400,400);
        scroller.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        this.setTop(header);
        this.setCenter(scroller);
        this.setBottom(footer);
        newButton = footer.getNewButton();
        logOutButton = footer.getLogOutButton();
        BorderPane.setAlignment(this, Pos.CENTER);
        
    }

    public BorderPane getRoot() {
        this.updateDisplay("none");
        return this;
    }

    public void updateDisplay(String filter) {
        recipeList.update(filter);
        this.setCenter(recipeList);
    }

    public void setNewRecipeButtonAction(EventHandler<ActionEvent> eventHandler) {
        newButton.setOnAction(eventHandler);
    }

    public void setLogOutButtonAction(EventHandler<ActionEvent> eventHandler) {
        logOutButton.setOnAction(eventHandler);
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
