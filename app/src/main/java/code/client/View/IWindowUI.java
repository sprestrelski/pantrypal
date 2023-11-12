package code.client.View;

import java.util.ArrayList;

import javafx.scene.Scene;

/**
 * This interface is used to maintain a list of UI screens
 * of different windows and provide them each access to
 * changing the display screen.
 */
public interface IWindowUI {

    /**
     * When this method is called, the caller UI becomes the root of the
     * main scene displayed to the User
     * 
     * @param scene - main scene variable.
     */
    public void setRoot(Scene scene);

    /**
     * Gives all windows access to the list of screen displays.
     * 
     * @param scenes - list of all different screens
     */
    public void setScenes(ArrayList<IWindowUI> scenes);
}
