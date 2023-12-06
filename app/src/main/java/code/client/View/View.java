package code.client.View;

import java.io.IOException;
import java.net.URISyntaxException;

import code.server.Recipe;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuButton;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class View {
    private AppFrameHome home;
    private AppFrameMic audioCapture;
    private DetailsAppFrame detailedRecipe;
    private LoginUI login;
    private AccountCreationUI createAcc;
    private Scene mainScene;
    private OfflineUI offlineScreen;
    private LoadingUI loadingUI;
    private String blinkStyle, defaultButtonStyle, onStyle, offStyle;

    public View() throws IOException, URISyntaxException {
        offlineScreen = new OfflineUI();
        login = new LoginUI();
        home = new AppFrameHome();
        audioCapture = new AppFrameMic();
        detailedRecipe = new DetailsAppFrame();
        createAcc = new AccountCreationUI();
        loadingUI = new LoadingUI();
        defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 11 arial;";
        onStyle = "-fx-font-style: italic; -fx-background-color: #90EE90; -fx-font-weight: bold; -fx-font: 11 arial;";
        offStyle = "-fx-font-style: italic; -fx-background-color: #FF7377; -fx-font-weight: bold; -fx-font: 11 arial;";
    }

    public Parent getMainScene() {
        return mainScene.getRoot();
    }

    public void setScene(Scene scene) {
        mainScene = scene;
    }

    public void goToRecipeList() {
        mainScene.setRoot(home.getRoot());
    }

    public void goToLoading() {
        mainScene.setRoot(loadingUI);
    }

    public void goToAudioCapture() throws URISyntaxException, IOException {
        audioCapture = new AppFrameMic();
        mainScene.setRoot(audioCapture.getRoot());
    }

    public void goToDetailedView(Recipe recipe, boolean savedRecipe) {
        mainScene.setRoot(detailedRecipe.getRoot(recipe, savedRecipe));
    }

    public void goToCreateAcc() {
        mainScene.setRoot(createAcc.getRoot());
    }

    public void goToLoginUI() {
        mainScene.setRoot(login.getRoot());
    }

    public void goToOfflineUI() {
        mainScene.setRoot(offlineScreen);
    }

    public OfflineUI getOfflineUI() {
        return offlineScreen;
    }

    public RecipeListUI getRecipeButtons() {
        return home.getRecipeList();
    }

    public DetailsAppFrame getDetailedView() {
        return detailedRecipe;
    }

    public AppFrameMic getAppFrameMic() {
        return audioCapture;
    }

    public AppFrameHome getAppFrameHome() {
        return home;
    }

    public LoginUI getLoginUI() {
        return login;
    }

    public AccountCreationUI getAccountCreationUI() {
        return createAcc;
    }

    public void callSaveAnimation() {
        blinkStyle = "-fx-background-color: #00FFFF; -fx-border-width: 0;";
        Button saveButtonFromDetailed = detailedRecipe.getSaveButton();
        saveButtonFromDetailed.setStyle(blinkStyle);
        PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
        pause.setOnFinished(f -> saveButtonFromDetailed.setStyle(defaultButtonStyle));
        pause.play();
    }

    public void setActiveState(MenuButton items, int index) {
        for (int i = 0; i < 4; i++) {
            if (i == index) {
                items.getItems().get(i).setStyle("-fx-background-color: #90EE90");
            } else {
                items.getItems().get(i).setStyle("-fx-background-color: transparent;");
            }
        }
    }

    public void displaySharedRecipeUI(Hyperlink textArea) {
         String styleAlert = "-fx-background-color: #F1FFCB; -fx-font-weight: bold; -fx-font: 14 arial";

        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(textArea, 0, 0);
        gridPane.setStyle(styleAlert);
        gridPane.setPrefSize(220, 220);
        gridPane.setAlignment(Pos.TOP_CENTER);
        textArea.setTextAlignment(TextAlignment.CENTER);
        Button copyButton = new Button("Copy to Clipboard");
        copyButton.setOnAction(event -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(textArea.getText());
            clipboard.setContent(content);
        });
        gridPane.add(copyButton, 0, 3);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Share this recipe!");
        alert.setHeaderText("Share this recipe with a friend!");
        alert.getDialogPane().setContent(gridPane);
        alert.showAndWait();
    }

    public void changeEditButtonColor(Button edit) {
        if (detailedRecipe.getRecipeDetailsUI().isEditable()) {
            edit.setStyle(onStyle);
        } else {
            edit.setStyle(offStyle);
        }
    }

    public void showLoginSuccessPane(GridPane grid, boolean loginSuccessful) {
        Text successText;
        if (loginSuccessful) {
            successText = new Text("Login successful! Welcome to Pantry Pal.");
            successText.setFill(Color.GREEN);
        } else {
            successText = new Text("Account does not exist. Please try again.");
            successText.setFill(Color.RED);
        }

        successText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        grid.add(successText, 1, 6);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(successText.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(successText.opacityProperty(), 0.0)));
        timeline.play();
    }

    public void showErrorPane(GridPane grid, String errorMessage) {
        Text errorText = new Text(errorMessage);
        errorText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        errorText.setFill(Color.RED);

        grid.add(errorText, 1, 6);

        // Fade away after 5 seconds
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(errorText.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(errorText.opacityProperty(), 0.0)));
        timeline.play();
    }

    public void showSuccessPane(GridPane grid) {
        Text successText = new Text("Successfully created an account!\nPlease login to access it.");
        successText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        successText.setFill(Color.GREEN);

        grid.add(successText, 1, 6);

        // Fade away after 5 seconds
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(successText.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(successText.opacityProperty(), 0.0)));
        timeline.play();
    }
}
