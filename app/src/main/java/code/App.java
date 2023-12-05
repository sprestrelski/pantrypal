package code;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import code.client.Model.*;
import code.client.View.*;
import code.server.BaseServer;
import code.server.AppServer;
import code.client.Controllers.*;
import javafx.scene.Scene;
import code.server.IRecipeDb;
import code.server.mocking.MockServer;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

public class App extends Application {
    private IRecipeDb recipeDb;
    private BaseServer server;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // initDb(); To use CSV file
        // initServer();
        // server.start();
        drawUI(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void drawUI(Stage primaryStage) throws IOException, URISyntaxException {
        View view = new View();
        Model model = new Model();
        Scene login = new Scene(view.getLoginUI().getRoot());
        view.setScene(login);
        Controller controller = new Controller(view, model);

        ServerConnection connection = new ServerConnection("localhost", 8100);

        if (connection.isOnline()) {
            // System.out.println("Server is online");
            controller.addListenersToList();
        } else {
            // System.out.println("Server is offline");
            view.goToOfflineUI();
        }

        primaryStage.setScene(login);
        primaryStage.setTitle(AppConfig.APP_NAME);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(620);
        primaryStage.setMinHeight(620);
        primaryStage.setHeight(620);
        primaryStage.setWidth(620);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                // System.out.println("Stage is closing");
                primaryStage.close();
                System.exit(1);
            }
        });
    }

    private IRecipeDb initDb() throws IOException {
        recipeDb = new RecipeListDb();
        RecipeCSVReader csvReader = new RecipeCSVReader(new FileReader(AppConfig.RECIPE_CSV_FILE));
        csvReader.readRecipeDb(recipeDb);
        csvReader.close();
        return recipeDb;
    }

    private void initServer() throws IOException {
        if (AppConfig.MOCKING_ON) {
            server = new MockServer(AppConfig.SERVER_HOST, AppConfig.SERVER_PORT);
        } else {
            server = new AppServer(AppConfig.SERVER_HOST, AppConfig.SERVER_PORT);
        }
    }
}