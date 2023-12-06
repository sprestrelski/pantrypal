package code.client.View;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import java.io.File;

import code.AppConfig;

public class OfflineUI extends VBox {
    private final Label offlineLabel;
    private final ImageView offlineImg;

    OfflineUI() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(javafx.geometry.Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
        gridPane.setStyle("-fx-background-color: #F0F8FF;");
        offlineLabel = new Label("The server is offline, please check your connection or try again later!");
        gridPane.add(offlineLabel, 1, 1);
        // Show an image when the server is offline
        File file = new File(AppConfig.OFFLINE_IMG_FILE);
        offlineImg = new ImageView(new Image(file.toURI().toString()));
        offlineImg.prefWidth(500);
        gridPane.add(offlineImg, 1, 2);
        getChildren().addAll(gridPane);
        GridPane.setFillHeight(gridPane, true);
        GridPane.setFillWidth(gridPane, true);
        GridPane.setMargin(gridPane, new Insets(10, 10, 10, 10));
        this.setFillWidth(true);
    }
}
