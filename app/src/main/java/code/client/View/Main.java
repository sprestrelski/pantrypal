
package code.client.View;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.io.*;
import java.net.URISyntaxException;

import javafx.geometry.Pos;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import code.client.Model.Recipe;

class RecipeUI extends HBox {
    private Label recipeIndex;
    private Button deleteButton, detailsButton;
    private Recipe recipe;

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
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        this.detailsButton.setText(recipe.getTitle());
    }

    public int getRecipeIndex() {
        return Integer.parseInt(this.recipeIndex.getText());
    }

    public void setRecipeIndex(int num) {
        this.recipeIndex.setText(Integer.toString(num));
    }

    public Button getDetailsButton() {
        return this.detailsButton;
    }

    public Button getDeleteButton() {
        return this.deleteButton;
    }

    public String getRecipeName() {
        return this.recipe.getTitle();
    }

    public String getRecipeIngredients() {
        return this.recipe.getAllIngredients();
    }

    public String getRecipeInstructions() {
        return this.recipe.getAllInstructions();
    }
}

class RecipeList extends VBox {

    RecipeList() {
        this.setSpacing(5);
        this.setPrefSize(700, 600);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    /*
     * Update the indices of the recipes in the list whenever a new recipe is added
     * or removed
     */
    public void updateRecipeIndices() {
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof RecipeUI) {
                ((RecipeUI) this.getChildren().get(i)).setRecipeIndex(i + 1);
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

    /*
     * Save recipes to a file called "recipes.csv"
     */
    public void saveRecipes() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("recipes.csv"));
            writer.write("sep=|\n"); // use "|" as a delimeter for the csv files
            writer.write("Recipe Name| Ingredients| Instructions\n"); // add labels for the columns of the csv file
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

    /*
     * Save recipes to a string via the same method as file write.
     */
    public String saveRecipesToString() {
        String file = "";
        try {
            file += "sep=|\n"; // use "|" as a delimeter for the csv files
            file += "Recipe Name| Ingredients| Instructions\n"; // add labels for the columns of the csv file
            for (int i = 0; i < this.getChildren().size(); i++) {
                file += (((RecipeUI) this.getChildren().get(i)).getRecipeName() + "| ");
                file += (((RecipeUI) this.getChildren().get(i)).getRecipeIngredients() + "| ");
                file += (((RecipeUI) this.getChildren().get(i)).getRecipeInstructions() + "\n");
            }
        } catch (Exception e) {
            System.out.println("Recipes could not be saved.");
        }
        return file;
    }

}

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

class AppFrame extends BorderPane {
    private Header header;
    private Footer footer;
    private RecipeList recipeList;
    private Button deleteButton, detailsButton, newButton;
    private ArrayList<IWindowUI> scenes;
    private Scene mainScene;

    AppFrame() {
        header = new Header();
        recipeList = new RecipeList();
        loadRecipes();
        footer = new Footer();

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

    /*
     * Load recipes from a file called "recipes.csv"
     */
    public void loadRecipes() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("recipes.csv"));
            String line;
            String[] recipeInfo;
            reader.readLine(); // skip the line with the delimeter specifier
            reader.readLine(); // skip the line with the csv column labels
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                // System.out.println("Line: " + line);
                RecipeUI recipe = new RecipeUI();
                recipeInfo = line.split("\\| ");
                /*
                 * System.out.println("Size: " + recipeInfo.length + "Title: " + recipeInfo[0]);
                 * System.out.println("Ingredients: " + recipeInfo[1]);
                 * System.out.println("Instructions: " + recipeInfo[2]);
                 */
                // TODO recreate Recipe using delimiters of ";;". Done ? need to test
                Recipe temp = new Recipe(Integer.toString(counter), recipeInfo[0]);
                temp.setAllIngredients(recipeInfo[1]);
                temp.setAllInstructions(recipeInfo[2]);
                recipe.setRecipe(temp);
                recipeList.getChildren().add(recipe);
                addListeners(recipe);
                counter++;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Recipes could not be loaded.");
        }
        recipeList.updateRecipeIndices();
    }

    public void addListeners(RecipeUI savedRecipe) {
        if (savedRecipe != null) {
            addListenersInRecipe(savedRecipe);
            return;
        }
        newButton.setOnAction(create -> {
            RecipeUI recipe = new RecipeUI();
            recipeList.getChildren().add(0, recipe);

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
                // TODO: Add a way to switch to detailed recipe view

                DetailsAppFrame details = (DetailsAppFrame) scenes.get(2);

                details.setRecipeHolder(new RecipeDetailsUI(recipe.getRecipe())); // should have RecipeDetailsUI
                details.storeNewRecipeUI(recipeList, recipe);

                details.setRoot(mainScene); // Changes UI to Detailed Recipe Screen

            });

            deleteButton = recipe.getDeleteButton();
            deleteButton.setOnAction(delete -> {
                recipeList.getChildren().remove(recipe);
                recipeList.saveRecipes();
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

    public AppFrame getRoot() {
        return this;
    }

    public void setMain(Scene main) {
        mainScene = main;
    }
}

class HomeScreen implements IWindowUI {
    private AppFrame home;
    Scene holder;

    HomeScreen() {
        home = new AppFrame();
        holder = new Scene(home, 700, 600);
    }

    public Scene getSceneWindow() {
        return holder;
    }

    /**
     * This method provides the UI holder with the different scenes that can be
     * switched between.
     * 
     * @param scenes - list of different scenes to switch between.
     */
    public void setScenes(ArrayList<IWindowUI> scenes) {
        home.setScenes(scenes);
    }

    @Override
    public void setRoot(Scene scene) {
        scene.setRoot(home);
        home.setMain(scene);
    }

}

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        HomeScreen home = new HomeScreen();

        NewRecipeUI audioCapture = new NewRecipeUI();
        // Scene speaking = audioCapture.getScene();

        DetailsAppFrame details = new DetailsAppFrame();
        // Scene details = chatGPTed.getScene();

        ArrayList<IWindowUI> scenes = new ArrayList<IWindowUI>();

        scenes.add(home);
        scenes.add(audioCapture);
        scenes.add(details);
        // details.setRecipeHolder(new RecipeDetailsUI(new Recipe("2", "Testing")));
        // Can create observer, subject interface here
        home.setScenes(scenes);
        audioCapture.setScenes(scenes);
        details.setScenes(scenes);

        primaryStage.setTitle("PantryPal");
        // primaryStage.setScene(home.getSceneWindow());

        Scene main = home.getSceneWindow();
        home.setRoot(main);

        primaryStage.setScene(main);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
