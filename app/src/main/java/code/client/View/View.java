package code.client.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javax.swing.Action;

/*
 * Stores all the buttons and GUI 
 */
public class View {
    private Button startRecButton, stopRecButton;
    private Label recordingLabel, transcriptLabel;
    private GridPane grid;

    public View() {
        grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setHgap(10);
        grid.setVgap(10);

        String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
        String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";
        /*
         * Audio recording
         */
        startRecButton = new Button("Start");
        startRecButton.setStyle(defaultButtonStyle);

        stopRecButton = new Button("Stop");
        stopRecButton.setStyle(defaultButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);

        grid.add(startRecButton, 0, 3); // start recording
        grid.add(stopRecButton, 1, 3); // stop
        grid.add(recordingLabel, 0, 4);

        transcriptLabel = new Label("");
        grid.add(transcriptLabel, 0, 5);

    }

    public GridPane getGrid() {
        return grid;
    }

    public Label getRecordingLabel() {
        return recordingLabel;
    }

    public Label getTranscriptLabel() {
        return transcriptLabel;
    }

    public void setTranscriptLabel(String transcript) {
        transcriptLabel.setText(transcript);
    }

    public void setStartButtonAction(EventHandler<ActionEvent> eventHandler) {
        startRecButton.setOnAction(eventHandler);
    }

    public void setStopButtonAction(EventHandler<ActionEvent> eventHandler) {
        stopRecButton.setOnAction(eventHandler);
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}