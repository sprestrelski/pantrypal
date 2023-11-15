package code.client.View;

import code.client.Model.*;
import code.client.Controllers.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
// import javax.sound.sampled.*;

class MealTypeSelection extends GridPane {
    private final Label prompt;
    private final Button recordButton;
    private final ImageView microphone;
    private final TextArea mealTypeArea;

    // =================FIRST PROMPT=================//
    MealTypeSelection() {
        // Set the preferred vertical and horizontal gaps
        this.setVgap(20);
        this.setHgap(20);

        // Get a picture of a microphone for the voice recording button
        File file = new File("app/src/main/java/code/client/View/microphone.png");
        microphone = new ImageView(new Image(file.toURI().toString()));

        // Set the size of the microphone image
        microphone.setFitWidth(50);
        microphone.setFitHeight(50);
        microphone.setScaleX(1);
        microphone.setScaleY(1);

        // Create a recording button
        recordButton = new Button();
        recordButton.setGraphic(microphone);

        // Set the user prompt for meal type selection
        prompt = new Label("Select Meal Type (Breakfast, Lunch, or Dinner)");
        prompt.setStyle("-fx-font-size: 16;");
        prompt.setTextFill(Color.web("#FF0000"));

        // Set a textField for the meal type that was selected
        mealTypeArea = new TextArea();
        mealTypeArea.setPromptText("Meal Type");
        mealTypeArea.setStyle("-fx-font-size: 16"); // CHANGE 1 (FONT)
        mealTypeArea.setPrefWidth(300);
        mealTypeArea.setPrefHeight(50);
        mealTypeArea.setEditable(false);

        // Add all of the elements to the MealTypeSelection
        this.add(recordButton, 0, 0);
        this.add(prompt, 1, 0);
        this.add(mealTypeArea, 0, 1);
    }

    public TextArea getMealType() {
        return mealTypeArea;
    }

    public Button getRecordButton() {
        return recordButton;
    }
}

class IngredientsList extends GridPane {

    private Label prompt;
    private Button recordButton;
    private ImageView microphone;
    private TextArea ingredientsArea;

    // ==============SECOND PROMPT=================//
    IngredientsList() {
        // Set the preferred vertical and horizontal gaps
        this.setVgap(20);
        this.setHgap(20);
        // Get a picture of a microphone for the voice recording button
        File file = new File("app/src/main/java/code/client/View/microphone.png");
        microphone = new ImageView(new Image(file.toURI().toString()));
        // Set the size of the microphone image
        microphone.setFitWidth(50);
        microphone.setFitHeight(50);
        microphone.setScaleX(1);
        microphone.setScaleY(1);
        // Create a recording button
        recordButton = new Button();
        recordButton.setGraphic(microphone);
        // Set the user prompt for meal type selection
        prompt = new Label("Please List Your Ingredients");
        prompt.setStyle("-fx-font-size: 16;");
        prompt.setTextFill(Color.web("#FF0000")); // CHANGE 2 (COLOR)
        // Set a textField for the meal type that was selected
        ingredientsArea = new TextArea();
        ingredientsArea.setPromptText("Ingredients");
        ingredientsArea.setStyle("-fx-font-size: 16"); // change
        ingredientsArea.setPrefWidth(300); // CHANGE 3 (WIDTH OF PROMPT)
        ingredientsArea.setPrefHeight(50); // CHANGE
        ingredientsArea.setEditable(false);

        // Add all of the elements to the MealTypeSelection
        this.add(recordButton, 0, 0);
        this.add(prompt, 1, 0);
        this.add(ingredientsArea, 0, 1);
    }

    public TextArea getIngredients() {
        return ingredientsArea;
    }

    public Button getRecordButton() {
        return recordButton;
    }
}

class GPTRecipe extends GridPane {
    private Label recipeLabel;
    private TextField recipeField;

    GPTRecipe() {
        this.setVgap(20);
        recipeLabel = new Label("Here Is Your Recipe");
        recipeField = new TextField();
        recipeField.setPrefWidth(500); // change
        recipeField.setPrefHeight(200); // change
        recipeLabel.setStyle("-fx-font-size: 16"); // change
        recipeLabel.setTextFill(Color.web("#FF0000")); // change
        this.add(recipeLabel, 0, 0);
        this.add(recipeField, 0, 1);
    }
}

class HeaderMic extends HBox {
    HeaderMic() {
        this.setPrefSize(700, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Recipe Creation");
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER);
    }
}

class AppFrameMic extends BorderPane {
    // Helper variables for button functionality
    private boolean recording; // keeps track if the app is currently recording
    private String mealType; // stores the meal type specified by the user
    private String ingredients; // stores the ingredients listed out by the user
    // AppFrameMic elements
    private GridPane recipeCreationGrid;
    private HeaderMic header;
    private MealTypeSelection mealTypeSelection;
    private IngredientsList ingredientsList;
    private Button recordButton1, recordButton2, saveButton, backButton;

    // Scene Transitions
    private ArrayList<IWindowUI> scenes;
    private Scene mainScene;
    private RecipeUI newRecipe;
    private RecipeListUI list;

    AppFrameMic() throws URISyntaxException, IOException {
        backButton = new Button("Back");
        // backButton.setOnAction(e -> goBack());
        HBox backButtonContainer = new HBox(backButton);
        backButtonContainer.setPadding(new Insets(1)); // padding

        this.setStyle("-fx-background-color: #DAE5EA;"); // If want to change
        // background color
        header = new HeaderMic();
        mealTypeSelection = new MealTypeSelection();
        ingredientsList = new IngredientsList();

        recipeCreationGrid = new GridPane();
        recipeCreationGrid.setAlignment(Pos.CENTER);
        recipeCreationGrid.setVgap(10);
        recipeCreationGrid.setHgap(10);
        recipeCreationGrid.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        recipeCreationGrid.add(backButtonContainer, 0, 0);
        recipeCreationGrid.add(mealTypeSelection, 0, 1);
        recipeCreationGrid.add(ingredientsList, 0, 2);

        this.setTop(header);
        this.setCenter(recipeCreationGrid);

        recordButton1 = mealTypeSelection.getRecordButton();
        recordButton2 = ingredientsList.getRecordButton();

        // createButton = new Button("Create Recipe");
        // recipeCreationGrid.add(createButton, 0, 3);

        saveButton = new Button("Save Setup");
        recipeCreationGrid.add(saveButton, 0, 5);

        backButton = new Button("Back to List");
        recipeCreationGrid.add(backButton, 0, 0);
        addListeners();
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void storeNewRecipeUI(RecipeListUI list, RecipeUI recipeUI) {
        newRecipe = recipeUI;
        this.list = list;
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

    public void setMain(Scene main) {
        mainScene = main;
    }

    public void addListeners() throws IOException, URISyntaxException {

        AudioRecorder recorder = new AudioRecorder();
        VoiceToText whisperService = new WhisperService();

        recordButton1.setOnAction(event -> {
            if (!recording) {
                recorder.startRecording();
                recording = true;
            } else {
                recorder.stopRecording();
                recording = false;

                try {
                    whisperService.setConnection(new RealHttpConnection(WhisperService.API_ENDPOINT));
                    whisperService.sendHttpRequest();
                    mealType = whisperService.processAudio();
                } catch (IOException | URISyntaxException exception) {
                    exception.printStackTrace();
                }

                mealType = mealType.toLowerCase();
                if (mealType.contains("breakfast")) {
                    mealTypeSelection.getMealType().setText("Breakfast");
                } else if (mealType.contains("lunch")) {
                    mealTypeSelection.getMealType().setText("Lunch");
                } else if (mealType.contains("dinner")) {
                    mealTypeSelection.getMealType().setText("Dinner");
                } else {
                    showAlert("Input Error", "Please say a valid meal type!");
                    mealType = "";
                }
            }
        });

        recordButton2.setOnAction(event -> {
            if (!recording) {
                recorder.startRecording();
                recording = true;
            } else {
                recorder.stopRecording();
                recording = false;

                try {
                    whisperService.setConnection(new RealHttpConnection(WhisperService.API_ENDPOINT));
                    whisperService.sendHttpRequest();
                    ingredients = whisperService.processAudio();
                } catch (IOException | URISyntaxException exception) {
                    exception.printStackTrace();
                }

                // handles gibberish
                String nonAsciiCharactersRegex = "[^\\x00-\\x7F]";
                if (ingredients.matches(".*" + nonAsciiCharactersRegex + ".*")) {
                    showAlert("Input Error", "Please provide valid ingredients!");
                    ingredients = "";
                } else {
                    ingredientsList.getIngredients().setText(ingredients);
                }

            }
        });

        // createButton.setOnAction(e -> {

        // });
        backButton.setOnAction(e -> {
            list.getChildren().remove(newRecipe);
            HomeScreen home = (HomeScreen) scenes.get(0);
            home.setRoot(mainScene);
        });

        // CHANGE SCENE TO DETAILED RECIPE DISPLAY
        saveButton.setOnAction(e -> {
            if (mealType != "" && ingredients != "") {
                ITextToRecipe caller = new ChatGPTService();
                try {
                    String audioOutput1 = mealType;
                    String audioOutput2 = ingredients;// audio.processAudio();
                    String responseText = caller.getChatGPTResponse(audioOutput1, audioOutput2);
                    Recipe recipe = caller.mapResponseToRecipe(responseText);
                    RecipeDetailsUI detailsUI = new RecipeDetailsUI(recipe);

                    // gets the DetailsAppFrame
                    DetailsAppFrame details = (DetailsAppFrame) scenes.get(2);
                    details.setRecipeHolder(detailsUI); // should have RecipeDetailsUI
                    details.storeNewRecipeUI(list, newRecipe);
                    details.setRoot(mainScene); // Changes UI to Detailed Recipe Screen

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                showAlert("Input Error", "Invalid meal type or ingredients, please try again!");
            }

        });
    }
}

public class NewRecipeUI implements IWindowUI {
    private AppFrameMic root;

    public NewRecipeUI() throws URISyntaxException, IOException {
        root = new AppFrameMic();
    }

    public void storeNewRecipeUI(RecipeListUI list, RecipeUI recipeUI) {
        root.storeNewRecipeUI(list, recipeUI);
    }

    public Scene getSceneWindow() {
        Scene scene = new Scene(root, 700, 700);
        return scene;
    }

    /**
     * This method provides the UI holder with the different scenes that can be
     * switched between.
     * 
     * @param scenes - list of different scenes to switch between.
     * @param scenes - list of different scenes to switch between.
     */

    @Override
    public void setRoot(Scene scene) {
        scene.setRoot(root);
        root.setMain(scene);
    }

    public void setScenes(ArrayList<IWindowUI> scenes) {
        root.setScenes(scenes);
    }

}
