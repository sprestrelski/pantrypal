package code.client.View;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class loginUI extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Pantry Pal - Login");

        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Pantry Pal - Login");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        grid.add(titleText, 0, 0, 2, 1); 

        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 1);

        TextField usernameField = new TextField();
        grid.add(usernameField, 1, 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        CheckBox rememberCheckBox = new CheckBox("Remember my login");
        grid.add(rememberCheckBox, 1, 3);

        Button loginButton = new Button("Login");
        grid.add(loginButton, 1, 4);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            boolean rememberLogin = rememberCheckBox.isSelected();

            // Perform login logic here
            boolean loginSuccessful = performLogin(username, password);

            if (loginSuccessful) {
                showLoginSuccessPane(grid, true);
            } else {
                showLoginSuccessPane(grid, false);
            }
        });

        Scene scene = new Scene(grid, 700, 600); 
        this.primaryStage.setScene(scene);

        this.primaryStage.show();
    }

    private void showLoginSuccessPane(GridPane grid, boolean loginSuccessful) {
        Text successText;
        if (loginSuccessful) {
            successText = new Text("Login successful! Welcome to Pantry Pal.");
            successText.setFill(Color.GREEN);
        } else {
            successText = new Text("Account does not exist. Please try again.");
            successText.setFill(Color.RED);
        }

        successText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        grid.add(successText, 1, 5);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(successText.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(successText.opacityProperty(), 0.0))
        );
        timeline.play();
    }

    private boolean performLogin(String username, String password) {
        
        // Will add logic for failed login later
        return true;
    }
}

