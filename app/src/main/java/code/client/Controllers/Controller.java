package code.client.Controllers;

import code.client.Controllers.AudioRecorder;
import code.client.View.View;
import javafx.event.ActionEvent;

public class Controller {
    private View view;
    private AudioRecorder audioRecorder;

    public Controller(View view) {
        this.view = view;
        this.audioRecorder = new AudioRecorder();

        // recording buttons
        this.view.setStartButtonAction(this::handleStartButton);
        this.view.setStopButtonAction(this::handleStopButton);
    }

    private void handleStartButton(ActionEvent event) {
        audioRecorder.startRecording();
    }

    private void handleStopButton(ActionEvent event) {
        // Makes calls to View
        audioRecorder.stopRecording();
    }
    /*
     * private void handlePostButton(ActionEvent event) {
     * String language = view.getLanguage();
     * String year = view.getYear();
     * String response = model.performRequest("POST", language, year, null);
     * view.showAlert("Response", response);
     * }
     * 
     * private void handleGetButton(ActionEvent event) {
     * String query = view.getQuery();
     * String response = model.performRequest("GET", null, null, query);
     * view.showAlert("Response", response);
     * }
     * 
     * private void handlePutButton(ActionEvent event) {
     * String language = view.getLanguage();
     * String year = view.getYear();
     * String response = model.performRequest("PUT", language, year, null);
     * view.showAlert("Response", response);
     * }
     * 
     * private void handleDeleteButton(ActionEvent event) {
     * String query = view.getQuery();
     * String response = model.performRequest("DELETE", null, null, query);
     * view.showAlert("Response", response);
     * }
     */
}
