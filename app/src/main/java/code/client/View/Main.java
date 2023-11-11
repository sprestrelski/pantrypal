
package code.client.View;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.io.*;
import javafx.geometry.Pos;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import code.client.Model.Recipe;

class RecipeUI extends HBox {
    private Label recipeIndex;
    private Button deleteButton, detailsButton;
    private String recipeName, recipeIngredients, recipeInstructions;

    RecipeUI() {
        // Index of the recipe in the recipe list
        recipeIndex = new Label();
        recipeIndex.setPrefSize(30, 100);
        recipeIndex.setAlignment(Pos.CENTER);
        recipeIndex.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        // Button to enter detailed recipe display
        detailsButton = new Button();
        detailsButton.setPrefSize(570, 100);
        detailsButton.setAlignment(Pos.CENTER_LEFT);
        detailsButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        // Button to delete unwanted recipes from the recipe list
        deleteButton = new Button("Delete");
        deleteButton.setPrefSize(100, 100);
        deleteButton.setAlignment(Pos.CENTER);
        deleteButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        // Add all the elements to the recipe UI
        this.getChildren().addAll(recipeIndex, detailsButton, deleteButton);
        // Initialize the name, ingredients, and instructions to be empty strings
        recipeName = recipeIngredients = recipeInstructions = "";
    }

    public void setRecipeIndex(int num) {
        this.recipeIndex.setText(num + "");
    }

    public Button getDetailsButton() {
        return this.detailsButton;
    }

    public Button getDeleteButton() {
        return this.deleteButton;
    }

    public String getRecipeName() {
        return this.recipeName;
    }

    public String getRecipeIngredients() {
        return this.recipeIngredients;
    }

    public String getRecipeInstructions() {
        return this.recipeInstructions;
    }
}

class RecipeList extends VBox {

    RecipeList() {
        this.setSpacing(5);
        this.setPrefSize(700, 600);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    /*
     * Update the indices of the recipes in the list whenever a new recipe is added or removed
     */
    public void updateRecipeIndices() {
        int index = 1;
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof RecipeUI) {
                ((RecipeUI) this.getChildren().get(i)).setRecipeIndex(index);
                index++;
            }
        }
    }

    /*
     * Remove a recipe from the recipe list whenever the delete button is clicked
     */
    public void removeRecipe(int index) {
        this.getChildren().remove(index - 1);
        this.updateRecipeIndices();
    }

    public void loadRecipes() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("recipes.csv"));
            String line;
            String[] recipeInfo;
            reader.readLine(); // skip the line with the delimeter specifier
            reader.readLine(); // skip the line with the csv column labels
            while ((line = reader.readLine()) != null) {
                recipeInfo = line.split("| ");
                RecipeUI recipe = new RecipeUI();
                recipe.getRecipeName() = recipeInfo[0];
                recipe.getRecipeIngredients() = recipeInfo[1];
                recipe.getRecipeInstructions() = recipeInfo[2];
                recipe.getDeleteButton().setOnAction(e -> {
                    recipe.toggleSelected();
                });
                this.getChildren().add(recipe);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Recipes could not be loaded.");
        }
        this.updateRecipeIndices();
    }

    public void saveRecipes() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("recipes.csv"));
            writer.write("sep=|\n");
            writer.write("Recipe Name| Ingredients| Instructions\n");
            for (int i = 0; i < this.getChildren().size(); i++) {
                writer.write(((RecipeUI) this.getChildren().get(i)).getRecipeName() + "| ");
                writer.write(((RecipeUI) this.getChildren().get(i)).getRecipeIngredients() + "| ");
                writer.write(((RecipeUI) this.getChildren().get(i)).getRecipeInstructions() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Recipes could not be saved.");
        }
    }
}

class Footer extends HBox {
    // Buttons for creating a new recipe and saving the current recipe list
    private Button newButton, saveButton;

    Footer() {
        
        this.setPrefSize(700, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 11 arial;";

        newButton = new Button("New Recipe");
        newButton.setStyle(defaultButtonStyle);
        saveButton = new Button("Save Recipes");
        saveButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(newButton, saveButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getNewButton() {
        return newButton;
    }

    public Button getSaveButton() {
        return saveButton;
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

class AppFrame extends BorderPane implements IWindowUI {
    private Header header;
    private Footer footer;
    private RecipeList recipeList;
    private Button deleteButton, newButton, recipeButton, saveButton;
    private ArrayList<IWindowUI> scenes;
    private Stage primaryStage;

    AppFrame() {
        header = new Header();
        recipeList = new RecipeList();
        footer = new Footer();

        ScrollPane scroller = new ScrollPane(recipeList);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);

        this.setTop(header);
        this.setCenter(scroller);
        this.setBottom(footer);

        newButton = footer.getNewButton();
        saveButton = footer.getSaveButton();

        addListeners();
    }

    public Scene getSceneWindow() {
        return new Scene(this, 700, 600);
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
        newButton.setOnAction(e -> {
            RecipeUI recipe = new RecipeUI();
            recipeList.getChildren().add(recipe);

            deleteButton = recipe.getDeleteButton();
            deleteButton.setOnAction(e2 -> {
                recipeList.toggleSelected();
            });

            recipeButton = recipe.getRe

            recipeList.updateRecipeIndices();
            primaryStage.setScene(scenes.get(1).getSceneWindow());
        });

        deleteButton.setOnAction(e -> {
            recipeList.removeSelectedRecipes();
        });

        loadButton.setOnAction(e -> {
            recipeList.loadRecipes();
        });

        saveButton.setOnAction(e -> {
            recipeList.saveRecipes();
        });

        sortButton.setOnAction(e -> {
            recipeList.sortRecipes();
        });
    }
}

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppFrame home = new AppFrame();
        // Scene home = new Scene(root, 700, 600);

        NewRecipeUI audioCapture = new NewRecipeUI();
        // Scene speaking = audioCapture.getScene();

        DetailsAppFrame details = new DetailsAppFrame();
        // Scene details = chatGPTed.getScene();

        ArrayList<IWindowUI> scenes = new ArrayList<IWindowUI>();
        scenes.add(home);
        scenes.add(audioCapture);
        scenes.add(details);
        details.setRecipeHolder(new RecipeDetailsUI(new Recipe("2", "Testing")));
        // Can create observer, subject interface here
        home.setScenes(primaryStage, scenes);
        audioCapture.setScenes(primaryStage, scenes);
        details.setScenes(primaryStage, scenes);

        primaryStage.setTitle("Recipe Management App");
        primaryStage.setScene(home.getSceneWindow());
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
