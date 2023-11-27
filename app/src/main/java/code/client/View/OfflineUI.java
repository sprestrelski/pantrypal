package code.client.View;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.io.File;

public class OfflineUI extends HBox {
    private Label offlineText;
    private ImageView cat;

    OfflineUI() {
        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #F0F8FF;");

        offlineText = new Label("The server is offline, please check your connection or try again later!");
        grid.add(offlineText, 1, 1);

        File file = new File("app/src/main/java/code/client/View/cat.png");
        cat = new ImageView(new Image(file.toURI().toString()));
        grid.add(cat, 1, 2);
        this.getChildren().addAll(grid);
    }

}
