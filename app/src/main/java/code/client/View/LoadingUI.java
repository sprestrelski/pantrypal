package code.client.View;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import java.io.File;

import code.AppConfig;

public class LoadingUI extends VBox {
    private final Label loadingLabel;
    private final ImageView loadingImg;

    LoadingUI() {
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(620, 620);
        gridPane.setAlignment(javafx.geometry.Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
        gridPane.setStyle("-fx-background-color: #F0F8FF;");
        loadingLabel = new Label("Loading...");
        loadingLabel.setFont(Font.font("Comic Sans MS", 20)); // Adjust the size as needed
        gridPane.add(loadingLabel, 1, 1);
        // Show an image when the server is offline
        File file = new File(AppConfig.LOADING_IMG_FILE);
        loadingImg = new ImageView(new Image(file.toURI().toString()));
        loadingImg.setFitWidth(Region.USE_COMPUTED_SIZE);
        gridPane.add(loadingImg, 1, 2);
        GridPane.setFillWidth(gridPane, true);
        getChildren().addAll(gridPane);
        this.setFillWidth(true);
    }
}
