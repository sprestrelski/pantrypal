package code;

import javafx.application.Application;
import javafx.stage.Stage;

import code.client.Model.*;
import code.client.View.*;
import code.client.Controllers.*;
import javafx.scene.Scene;
import java.util.ArrayList;

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