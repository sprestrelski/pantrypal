package code.client.View;

import code.client.Model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
// import javax.sound.sampled.*;

class MealTypeSelection extends GridPane {
    private final Label prompt, recordingLabel;
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

        // Create a label that indicated if the app is currently recording
        recordingLabel = new Label("Recording...");
        recordingLabel.setTextFill(Color.web("#FF0000"));
        recordingLabel.setVisible(false);

        // Set the user prompt for meal type selection
        prompt = new Label("Select Meal Type (Breakfast, Lunch, or Dinner)");
        prompt.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        // prompt.setTextFill(Color.web("#FF0000"));

        // Set a textField for the meal type that was selected
        mealTypeArea = new TextArea();
        mealTypeArea.setPromptText("Meal Type");
        mealTypeArea.setStyle("-fx-font-size: 16"); // CHANGE 1 (FONT)
        mealTypeArea.setPrefWidth(300);
        mealTypeArea.setPrefHeight(50);
        mealTypeArea.setEditable(false);

        // Add all of the elements to the MealTypeSelection
        this.add(recordButton, 0, 0);
        this.add(recordingLabel, 0, 1);
        this.add(prompt, 0, 2);
        this.add(mealTypeArea, 0, 3);

    }

    public TextArea getMealType() {
        return mealTypeArea;
    }

    public Button getRecordButton() {
        return recordButton;
    }

    public Label getRecordingLabel() {
        return recordingLabel;
    }
}

class IngredientsList extends GridPane {

    private Label prompt, recordingLabel;
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

        // Create a label that indicated if the app is currently recording
        recordingLabel = new Label("Recording...");
        recordingLabel.setTextFill(Color.web("#FF0000"));
        recordingLabel.setVisible(false);

        // Set the user prompt for meal type selection
        prompt = new Label("Please List Your Ingredients");
        prompt.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        // prompt.setTextFill(Color.web("#FF0000"));

        // Set a textField for the meal type that was selected
        ingredientsArea = new TextArea();
        ingredientsArea.setPromptText("Ingredients");
        ingredientsArea.setStyle("-fx-font-size: 16"); // change
        ingredientsArea.setPrefWidth(300); // CHANGE 3 (WIDTH OF PROMPT)
        ingredientsArea.setPrefHeight(50); // CHANGE
        ingredientsArea.setEditable(false);

        // Add all of the elements to the MealTypeSelection
        this.add(recordButton, 0, 0);
        this.add(recordingLabel, 0, 1);
        this.add(prompt, 0, 2);
        this.add(ingredientsArea, 0, 3);
    }

    public TextArea getIngredients() {
        return ingredientsArea;
    }

    public Button getRecordButton() {
        return recordButton;
    }

    public Label getRecordingLabel() {
        return recordingLabel;
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

public class AppFrameMic extends BorderPane {
    // Helper variables for button functionality
    private boolean recording; // keeps track if the app is currently recording
    private String mealType; // stores the meal type specified by the user
    private String ingredients; // stores the ingredients listed out by the user
    // AppFrameMic elements
    private GridPane recipeCreationGrid;
    private HeaderMic header;
    private MealTypeSelection mealTypeSelection;
    private IngredientsList ingredientsList;
    private Button recordMealTypeButton, recordIngredientsButton, goToDetailedButton, backButton;
    private Label recordingMealTypeLabel, recordingIngredientsLabel;
    private final AudioRecorder recorder = new AudioRecorder();
    private final VoiceToText voiceToText = new WhisperService();

    AppFrameMic() throws URISyntaxException, IOException {
        setStyle("-fx-background-color: #DAE5EA;"); // If want to change
        // background color
        header = new HeaderMic();
        mealTypeSelection = new MealTypeSelection();
        ingredientsList = new IngredientsList();

        recipeCreationGrid = new GridPane();
        recipeCreationGrid.setAlignment(Pos.CENTER);
        recipeCreationGrid.setVgap(10);
        recipeCreationGrid.setHgap(10);
        recipeCreationGrid.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        recipeCreationGrid.add(mealTypeSelection, 0, 1);
        recipeCreationGrid.add(ingredientsList, 0, 2);

        this.setTop(header);
        this.setCenter(recipeCreationGrid);

        recordMealTypeButton = mealTypeSelection.getRecordButton();
        recordingMealTypeLabel = mealTypeSelection.getRecordingLabel();
        recordIngredientsButton = ingredientsList.getRecordButton();
        recordingIngredientsLabel = ingredientsList.getRecordingLabel();

        // createButton = new Button("Create Recipe");
        // recipeCreationGrid.add(createButton, 0, 3);

        goToDetailedButton = new Button("See Detailed Recipe");
        recipeCreationGrid.add(goToDetailedButton, 0, 5);

        backButton = new Button("Back to List");
        recipeCreationGrid.add(backButton, 0, 0);
        addListeners();
    }

    private void recordMealType() {
        if (!recording) {
            recorder.startRecording();
            recording = true;
            recordMealTypeButton.setStyle("-fx-background-color: #FF0000;");
            recordingMealTypeLabel.setVisible(true);
            // recordingLabel1.setStyle("-fx-font-color: #FF0000;");
        } else {
            recorder.stopRecording();
            recording = false;
            recordMealTypeButton.setStyle("");
            recordingMealTypeLabel.setVisible(false);
            // recordingLabel1.setStyle("");

            try {
                mealType = voiceToText.processAudio().toLowerCase();
                // type check
                if (mealType.contains("breakfast")) {
                    mealTypeSelection.getMealType().setText("Breakfast");
                } else if (mealType.contains("lunch")) {
                    mealTypeSelection.getMealType().setText("Lunch");
                } else if (mealType.contains("dinner")) {
                    mealTypeSelection.getMealType().setText("Dinner");
                } else {
                    AppAlert.show("Input Error", "Please say a valid meal type!");
                    mealType = null;
                }
            } catch (IOException | URISyntaxException exception) {
                AppAlert.show("Connection Error", "Something went wrong. Please check your connection and try again.");
                exception.printStackTrace();
            }
        }
    }

    private void recordIngredients() {
        if (!recording) {
            recorder.startRecording();
            recording = true;
            recordIngredientsButton.setStyle("-fx-background-color: #FF0000;");
            recordingIngredientsLabel.setVisible(true);
            // recordingLabel2.setStyle("-fx-background-color: #FF0000;");
        } else {
            recorder.stopRecording();
            recording = false;
            recordIngredientsButton.setStyle("");
            recordingIngredientsLabel.setVisible(false);
            // recordingLabel2.setStyle("");

            try {
                ingredients = voiceToText.processAudio();
                String nonAsciiCharactersRegex = "[^\\x00-\\x7F]";

                if (ingredients.matches(".*" + nonAsciiCharactersRegex + ".*") ||
                        ingredients.trim().isEmpty() ||
                        ingredients.contains("you")) {
                    AppAlert.show("Input Error", "Please provide valid ingredients!");
                    ingredients = null;
                } else {
                    ingredientsList.getIngredients().setText(ingredients);
                }
            } catch (IOException | URISyntaxException exception) {
                AppAlert.show("Connection Error", "Something went wrong. Please check your connection and try again.");
                exception.printStackTrace();
            }
        }
    }

    public void addListeners() throws IOException, URISyntaxException {
        recordMealTypeButton.setOnAction(event -> {
            recordMealType();
        });

        recordIngredientsButton.setOnAction(event -> {
            recordIngredients();
        });

    }

    public void setGoToDetailedButtonAction(EventHandler<ActionEvent> eventHandler) {
        goToDetailedButton.setOnAction(eventHandler);
    }

    public void setGoToHomeButtonAction(EventHandler<ActionEvent> eventHandler) {
        backButton.setOnAction(eventHandler);
    }

    // recordButton1, recordButton2, saveButton, backButton;
    public Button getRecButton1() {
        return recordMealTypeButton;
    }

    public Button getRecButton2() {
        return recordIngredientsButton;
    }

    public StackPane getRoot() {
        StackPane stack = new StackPane();
        stack.getChildren().add(this);
        return stack;
    }

    public ArrayList<String> getVoiceResponse() {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(mealType);
        temp.add(ingredients);
        return temp;
    }

}
