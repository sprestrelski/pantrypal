package code.client.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class AccountCreationUI {
    private Button createAccountButton;
    private TextField usernameField;
    private PasswordField passwordField;
    private GridPane grid;
    private Hyperlink goToLogin;

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
        createAccountButton.setDefaultButton(true);
        grid.add(createAccountButton, 1, 3);

        goToLogin = new Hyperlink("Click to return to login.");

        FlowPane flow = new FlowPane();
        flow.getChildren().addAll(
                new Text("Already have an account? "), goToLogin);
        grid.add(flow, 1, 5);
        GridPane.setFillWidth(grid, true);
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

    public void setGoToLoginAction(EventHandler<ActionEvent> eventHandler) {
        goToLogin.setOnAction(eventHandler);
    }
}
