package code.client.View;

import java.util.*;
import java.io.*;
import java.nio.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import code.client.Model.Account;

public class LoginUI {

    private boolean rememberLogin, accountSaved = false;
    private Account savedAccount;
    private Hyperlink goToCreate;
    private Button loginButton;
    private TextField usernameField;
    private PasswordField passwordField;
    private GridPane grid;

    public static final String CSVFILE = "usercredentials.csv";

    LoginUI() {
        loadCredentials();
        if (savedAccount != null) {
            accountSaved = true;
        }

        // this.primaryStage.setTitle("Pantry Pal - Login");

        grid = new GridPane();
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

        usernameField = new TextField();
        grid.add(usernameField, 1, 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);

        passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        CheckBox rememberCheckBox = new CheckBox("Remember my login");
        grid.add(rememberCheckBox, 1, 3);

        rememberCheckBox.setOnAction(e -> {
            rememberLogin = rememberCheckBox.isSelected();
            System.out.println("Remember Login: " + rememberLogin);
        });

        loginButton = new Button("Login");
        grid.add(loginButton, 1, 4);

        goToCreate = new Hyperlink("Click here");
        ;

        FlowPane flow = new FlowPane();
        flow.getChildren().addAll(
                new Text("Don't have an account? "), goToCreate);
        grid.add(flow, 1, 5);

        loginButton.setOnAction(e -> {

        });

        if (accountSaved) {
            usernameField.setText(savedAccount.getUsername());
            passwordField.setText(savedAccount.getPassword());
        }

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

    public boolean getRememberLogin() {
        return rememberLogin;
    }

    public void setGoToCreateAction(EventHandler<ActionEvent> eventHandler) {
        goToCreate.setOnAction(eventHandler);
    }

    public void setLoginButtonAction(EventHandler<ActionEvent> eventHandler) {
        loginButton.setOnAction(eventHandler);
    }

    public void loadCredentials() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(CSVFILE));
            String line;
            String[] credentials;
            while ((line = reader.readLine()) != null) {
                credentials = line.split("\\|");
                savedAccount = new Account(credentials[0], credentials[1]);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("No account credentials saved currently.");
        }
    }
}
