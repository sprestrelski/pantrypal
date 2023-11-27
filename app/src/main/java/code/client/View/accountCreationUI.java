package code.client.View;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class AccountCreationUI {
    private Button createAccountButton;
    private TextField usernameField;
    private PasswordField passwordField;
    private GridPane grid;

    AccountCreationUI() {
        grid = new GridPane();
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

        usernameField = new TextField();
        grid.add(usernameField, 1, 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);

        passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        createAccountButton = new Button("Create Account");
        grid.add(createAccountButton, 1, 3);
    }
    public GridPane getRoot() {
        return grid;
    }

    public TextField getUsernameTextField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public void setCreateAccountButtonAction(EventHandler<ActionEvent> eventHandler) {
        createAccountButton.setOnAction(eventHandler);
    }
}