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

public class Ingredients extends GridPane {

    private Label prompt, recordingLabel;
    private Button recordButton;
    private ImageView microphone;
    private TextArea ingredientsArea;

    // ==============SECOND PROMPT=================//
    Ingredients() {
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
        ingredientsArea.setEditable(true);

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
