package code.client.View;

import javafx.scene.Scene;

import java.io.IOException;
import java.util.ArrayList;

public class HomeScreen implements IWindowUI {
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
        home.updateDisplay();
        home.setMain(scene);
    }

}

