package code.client.View;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonAreaLayout;

import code.client.Model.Recipe;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import code.client.View.AppFrameMic;

public class View {
    //private IWindowUI home, audioCapture, detailedRecipe, currentScene;
    private AppFrameHome home;
    private AppFrameMic audioCapture;
    private DetailsAppFrame detailedRecipe;
    private Button newRecipeButton, saveButton, editButton, backToHomeButton;
    private Scene mainScene;

    public View() throws IOException, URISyntaxException {
        home = new AppFrameHome();
        audioCapture = new AppFrameMic();
        detailedRecipe = new DetailsAppFrame();
    }

    public void setScene(Scene scene) {
        mainScene = scene;
    }

    public void goToRecipeList() {
        mainScene.setRoot(home.getRoot());
    }

    public void goToAudioCapture() throws URISyntaxException, IOException {
        audioCapture = new AppFrameMic();
        mainScene.setRoot(audioCapture.getRoot());
    }

    public void goToDetailedView(Recipe recipe, boolean savedRecipe) {
        mainScene.setRoot(detailedRecipe.getRoot(recipe, savedRecipe));
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

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
