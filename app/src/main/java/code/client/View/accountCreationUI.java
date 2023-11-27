package code.client.View;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

import code.client.Model.Account;

public class AccountCreationUI extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Pantry Pal - Account Creation");

        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Pantry Pal - Create an Account");
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

        Button createAccountButton = new Button("Create Account");
        grid.add(createAccountButton, 1, 3);

        // Simulating existing usernames
        String[] existingUsernames = {"user1", "user2", "user3"};

        createAccountButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                // Display an error message if username or password is empty
                showErrorPane(grid, "Error. Please provide a username and password.");
            } else if (isUsernameTaken(username, existingUsernames)) {
                // Display an error message if the username is already taken
                showErrorPane(grid, "Error. This username is already taken. Please choose another one.");
            } else {
                // Continue with account creation logic
                System.out.println("Account Created!\nUsername: " + username + "\nPassword: " + password);

                // Show success message
                showSuccessPane(grid);
            }
        });

        Scene scene = new Scene(grid, 700, 600);
        this.primaryStage.setScene(scene);

        this.primaryStage.show();
    }

    private boolean isUsernameTaken(String username, String[] existingUsernames) {
        // Check if the username is already taken
        // temporary logic, no database yet
        for (String existingUsername : existingUsernames) {
            if (existingUsername.equals(username)) {
                return true;
            }
        }
        return false;
    }

    private void showErrorPane(GridPane grid, String errorMessage) {
        Text errorText = new Text(errorMessage);
        errorText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        errorText.setFill(Color.RED);

        grid.add(errorText, 1, 4);

        // Fade away after 5 seconds
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(errorText.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(errorText.opacityProperty(), 0.0))
        );
        timeline.play();
    }

    private void showSuccessPane(GridPane grid) {
        Text successText = new Text("Successfully created an account!\nPlease login to access it.");
        successText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        successText.setFill(Color.GREEN);

        grid.add(successText, 1, 4);

        // Fade away after 5 seconds
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(successText.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(successText.opacityProperty(), 0.0))
        );
        timeline.play();
    }
}