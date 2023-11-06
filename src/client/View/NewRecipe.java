package client.View;

import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class NewRecipe extends Application {

    @Override
    public void start(Stage primaryStage) {
        
        VBox root = new VBox();
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #F0F8FF;");

        
        Text title = new Text("New Recipe Setup");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        root.getChildren().add(title);

   
        VBox setupContainer = new VBox();
        setupContainer.setSpacing(10);

        //PROMPT 1
        HBox option1Container = new HBox(10);
        Label option1Label = new Label("1) Breakfast, Lunch, or Dinner?");
        option1Label.setStyle("-fx-font-size: 16;"); // Increase the font size
        Button microphoneButton1 = new Button();
        Image microphoneImage = new Image(getClass().getResourceAsStream(".\\microphone.png"));

        ImageView microphoneImageView1 = new ImageView(microphoneImage);
        microphoneImageView1.setFitHeight(30);
        microphoneImageView1.setFitWidth(30);
        microphoneButton1.setGraphic(microphoneImageView1);
        option1Container.getChildren().addAll(option1Label, microphoneButton1);

        // PROMPT 2
        HBox option2Container = new HBox(10);
        Label option2Label = new Label("2) List the ingredients you have");
        option2Label.setStyle("-fx-font-size: 16;"); // Increase the font size
        Button microphoneButton2 = new Button();
        ImageView microphoneImageView2 = new ImageView(microphoneImage);
        microphoneImageView2.setFitHeight(30);
        microphoneImageView2.setFitWidth(30);
        microphoneButton2.setGraphic(microphoneImageView2);
        option2Container.getChildren().addAll(option2Label, microphoneButton2);

        setupContainer.getChildren().addAll(option1Container, option2Container);

    
        root.getChildren().add(setupContainer);

     
        Button saveButton = new Button("Save Setup");
        root.getChildren().add(saveButton);


        Scene scene = new Scene(root, 700, 600);
        primaryStage.setTitle("New Recipe Setup");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

