package code;

import javafx.application.Application;
import javafx.stage.Stage;

import code.client.Model.*;
import code.client.View.*;
import code.client.Controllers.*;
import javafx.scene.Scene;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view, model);
        Scene login = new Scene(view.getLoginUI().getRoot());
        view.setScene(login);
        // Scene main = new Scene(view.getAppFrameHome().getRoot());
        // view.setScene(main);
        controller.addListenersToList();

        primaryStage.setScene(login);
        primaryStage.setTitle("Pantry Pal");
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(620);
        primaryStage.setMinHeight(620);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}