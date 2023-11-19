package code.client.View;

import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.ArrayList;

public class HomeScreen {
    private AppFrameHome home;
    private Scene holder;

    public HomeScreen() throws IOException {
        home = new AppFrameHome();
        holder = new Scene(home, 700, 600);
    }

    public Scene getSceneWindow() {
        return holder;
    }

    public AppFrameHome getAppFrameHome() {
        return home;
    }


    public Button getNewButton() {
        return home.getNewButton();
    }

}

