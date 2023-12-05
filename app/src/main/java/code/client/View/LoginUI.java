package code.client.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import code.client.Model.Account;

public class LoginUI {

    private boolean rememberLogin;
    private Hyperlink goToCreate;
    private Button loginButton;
    private TextField usernameField;
    private PasswordField passwordField;
    private GridPane grid;

    public LoginUI() {
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
        loginButton.setDefaultButton(true);
        grid.add(loginButton, 1, 4);

        goToCreate = new Hyperlink("Click here");

        FlowPane flow = new FlowPane();
        flow.getChildren().addAll(
                new Text("Don't have an account? "), goToCreate);
        grid.add(flow, 1, 5);
    }

    public void setLoginCreds(Account account) {
        usernameField.setText(account.getUsername());
        passwordField.setText(account.getPassword());
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

}
