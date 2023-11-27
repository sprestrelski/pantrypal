package code;

import javafx.application.Application;
import javafx.stage.Stage;

import code.client.Model.*;
import code.client.View.*;
import code.server.AppServer;
import code.client.Controllers.*;
import javafx.scene.Scene;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

public class App extends Application {
    private IRecipeDb recipeDb;
    private AppServer server;

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUpDb();
        startServer();
        drawUI(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void drawUI(Stage primaryStage) throws IOException, URISyntaxException {
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view, model);

        Scene scene = new Scene(view.getAppFrameHome().getRoot());
        view.setScene(scene);
        controller.addListenersToList();

        primaryStage.setScene(scene);
        primaryStage.setTitle(AppConfig.APP_NAME);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private IRecipeDb setUpDb() throws IOException {
        recipeDb = new RecipeListDb();
        RecipeCSVReader csvReader = new RecipeCSVReader(new FileReader(AppConfig.CSV_FILE));
        csvReader.readRecipeDb(recipeDb);
        csvReader.close();
        return recipeDb;
    }

    private void startServer() throws IOException {
        server = new AppServer(recipeDb);
        server.start();
    }
}