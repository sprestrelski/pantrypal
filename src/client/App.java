package client;

import client.Controllers.Controller;
import client.Model.Model;
import client.View.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view, model);

        Scene scene = new Scene(view.getGrid(), 1200, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("PantryPal");
        primaryStage.show();

    }
}