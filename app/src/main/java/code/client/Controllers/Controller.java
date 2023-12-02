package code.client.Controllers;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;

import code.client.View.RecipeListUI;
import code.client.View.RecipeUI;
import code.client.View.View;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import code.client.Model.*;
import code.client.View.AppAlert;

public class Controller {
    private Account account;
    private Model model;
    private View view;
    private RecipeCSVWriter recipeWriter;
    private String title;
    private String defaultButtonStyle, onStyle, offStyle, blinkStyle;
    public static final String CSVFILE = "usercredentials.csv";

    public Controller(View view, Model model) {

        this.view = view;
        this.model = model;

        defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 11 arial;";
        onStyle = "-fx-font-style: italic; -fx-background-color: #90EE90; -fx-font-weight: bold; -fx-font: 11 arial;";
        offStyle = "-fx-font-style: italic; -fx-background-color: #FF7377; -fx-font-weight: bold; -fx-font: 11 arial;";
        blinkStyle = "-fx-background-color: #00FFFF; -fx-border-width: 0;";

        this.view.getAppFrameHome().setNewRecipeButtonAction(event -> {
            try {
                handleNewButton(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.view.getAppFrameHome().setLogOutButtonAction(event -> {
            handleLogOutOutButton(event);
        });

        this.view.getAccountCreationUI().setCreateAccountButtonAction(this::handleCreateAcc);
        this.view.getLoginUI().setGoToCreateAction(this::handleGoToCreateLogin);
        this.view.getLoginUI().setLoginButtonAction(this::handleLoginButton);
        loadCredentials();
        if(account != null) {
            this.view.getLoginUI().setLoginCreds(account);
            this.view.goToRecipeList();
            addListenersToList();
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private void handleRecipePostButton(ActionEvent event) throws IOException {
        Recipe postedRecipe = view.getDetailedView().getDisplayedRecipe();

        Button saveButtonFromDetailed = view.getDetailedView().getSaveButton();
        saveButtonFromDetailed.setStyle(blinkStyle);
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(f -> saveButtonFromDetailed.setStyle(defaultButtonStyle));
        pause.play();

        Writer writer = new StringWriter();
        recipeWriter = new RecipeCSVWriter(writer);
        recipeWriter.writeRecipe(postedRecipe);

        String recipe = writer.toString();
        // Debugging
        System.out.println("Posting: " + recipe);

        model.performRecipeRequest("POST", recipe, null);
    }

    private void handleGetButton(ActionEvent event) {
        String uuid = UUID.fromString(title).toString();
        model.performRecipeRequest("GET", null, uuid);
    }

    private void handleDeleteButton(ActionEvent event) {
        String uuid = UUID.fromString(title).toString();
        model.performRecipeRequest("DELETE", null, uuid);
    }

    private void handleNewButton(ActionEvent event) throws URISyntaxException, IOException {
        view.goToAudioCapture();
        this.view.getAppFrameMic().setGoToDetailedButtonAction(this::handleDetailedViewFromNewRecipeButton);
        this.view.getAppFrameMic().setGoToHomeButtonAction(this::handleHomeButton);

    }

    private void handleLogOutOutButton(ActionEvent event) {
        clearCredentials();
        view.goToLoginUI();
        view.getLoginUI().getUsernameTextField().clear();
        view.getLoginUI().getPasswordField().clear();
    }

    private void handleHomeButton(ActionEvent event) {
        view.goToRecipeList();
        addListenersToList();
    }

    public void addListenersToList() {
        RecipeListUI list = view.getAppFrameHome().getRecipeList();
        for (int i = 0; i < list.getChildren().size(); i++) {
            RecipeUI currRecipe = (RecipeUI) list.getChildren().get(i);
            currRecipe.getDeleteButton().setOnAction(e -> {
                setTitle(currRecipe.getRecipeName());
                String uuid = currRecipe.getId();
                model.performRecipeRequest("DELETE", null, uuid);
            });
            currRecipe.getDetailsButton().setOnAction(e -> {
                view.goToDetailedView(currRecipe.getRecipe(), true);
                view.getDetailedView().getRecipeDetailsUI().setEditable(false);
                changeEditButtonColor(view.getDetailedView().getEditButton());
                handleDetailedViewListeners();
            });
        }
    }

    private void handleDetailedViewFromNewRecipeButton(ActionEvent event) {
        // Get ChatGPT response from the Model
        List<String> inputs = view.getAppFrameMic().getVoiceResponse();

        String mealType = inputs.get(0);
        String ingredients = inputs.get(1);
        if (mealType != null && ingredients != null) {
            TextToRecipe caller = new MockGPTService();// new ChatGPTService();
            try {
                String audioOutput1 = mealType;
                String audioOutput2 = ingredients;// audio.processAudio();
                String responseText = caller.getResponse(audioOutput1, audioOutput2);
                Recipe chatGPTrecipe = caller.mapResponseToRecipe(mealType, responseText);

                // TODO Changes UI to Detailed Recipe Screen
                view.goToDetailedView(chatGPTrecipe, false);
                view.getDetailedView().getRecipeDetailsUI().setEditable(false);
                handleDetailedViewListeners();

            } catch (IOException | URISyntaxException | InterruptedException exception) {
                AppAlert.show("Connection Error", "Something went wrong. Please check your connection and try again.");
                exception.printStackTrace();
            }
        } else {
            AppAlert.show("Input Error", "Invalid meal type or ingredients, please try again!");
        }
    }

    private void handleDetailedViewListeners() {
        // Saving recipe or editing recipe from Detailed View
        this.view.getDetailedView().setPostButtonAction(event -> {
            try {
                handleRecipePostButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.view.getDetailedView().setEditButtonAction(this::handleEditButton);
        this.view.getDetailedView().setDeleteButtonAction(this::handleDeleteButton);
        this.view.getDetailedView().setHomeButtonAction(this::handleHomeButton);
    }

    private void handleEditButton(ActionEvent event) {
        Button edit = view.getDetailedView().getEditButton();
        view.getDetailedView().getRecipeDetailsUI().setEditable();
        changeEditButtonColor(edit);
    }

    private void changeEditButtonColor(Button edit) {
        if (view.getDetailedView().getRecipeDetailsUI().isEditable()) {
            edit.setStyle(onStyle);
        } else {
            edit.setStyle(offStyle);
        }
    }

    private void handleGoToCreateLogin(ActionEvent event) {
        view.goToCreateAcc();
    }

    ////////////////////////////////////////
    private void handleCreateAcc(ActionEvent event) {
        // Simulating existing usernames
        GridPane grid = view.getAccountCreationUI().getRoot();
        String username = view.getAccountCreationUI().getUsernameTextField().getText();
        String password = view.getAccountCreationUI().getPasswordField().getText();

        if (username.isEmpty() || password.isEmpty()) {
            // Display an error message if username or password is empty
            showErrorPane(grid, "Error. Please provide a username and password.");
        } else if (isUsernameTaken(username)) {
            // Display an error message if the username is already taken
            showErrorPane(grid, "Error. This username is already taken. Please choose another one.");
        } else {
            // Continue with account creation logic
            System.out.println("Account Created!\nUsername: " + username + "\nPassword: " + password);
            model.performUserRequest("PUT", username, password);
            // Show success message
            showSuccessPane(grid);
            view.goToLoginUI();
        }
    }

    private void showErrorPane(GridPane grid, String errorMessage) {
        Text errorText = new Text(errorMessage);
        errorText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        errorText.setFill(Color.RED);

        grid.add(errorText, 1, 6);

        // Fade away after 5 seconds
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(errorText.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(errorText.opacityProperty(), 0.0)));
        timeline.play();
    }

    private void showSuccessPane(GridPane grid) {
        Text successText = new Text("Successfully created an account!\nPlease login to access it.");
        successText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        successText.setFill(Color.GREEN);

        grid.add(successText, 1, 6);

        // Fade away after 5 seconds
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(successText.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(successText.opacityProperty(), 0.0)));
        timeline.play();
    }

    private boolean isUsernameTaken(String username) {
        // Check if the username is already taken
        // temporary logic, no database yet
        String response = model.performUserRequest("GET", username, "");
        System.out.println("Response for usernameTaken : " + response);
        return (response.equals("Username is taken"));
    }
    ////////////////////////////////////////

    private void handleLoginButton(ActionEvent event) {
        String username = view.getLoginUI().getUsernameTextField().getText();
        String password = view.getLoginUI().getPasswordField().getText();
        GridPane grid = view.getLoginUI().getRoot();
        // Perform login logic here
        if (username.isEmpty() || password.isEmpty()) {
            // Display an error message if username or password is empty
            showErrorPane(grid, "Error. Please provide a username and password.");
        } else {
            boolean loginSuccessful = performLogin(username, password);

            if (loginSuccessful) {
                showLoginSuccessPane(grid, true); // useless

                view.goToRecipeList();
                addListenersToList();

                if (!view.getLoginUI().getRememberLogin()) {
                    clearCredentials();
                }
                else {
                    saveCredentials(account);
                }
            } else {
                showLoginSuccessPane(grid, false);
            }
        }
    }

    private void clearCredentials() {
        try (FileWriter writer = new FileWriter("userCredentials.csv", false)) {
            writer.write("");
            writer.flush();
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            System.out.println("Account credentials could not be erased.");
        }
    }

    private void saveCredentials(Account acc) {
        try (FileWriter writer = new FileWriter("userCredentials.csv", true)) {
            writer.append(acc.getUsername())
                    .append("|")
                    .append(acc.getPassword())
                    .append("|")
                    .append(acc.getId().toString());
            writer.flush();
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            System.out.println("Account credentials could not be saved.");
        }
    }
    private void loadCredentials() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(CSVFILE));
            String line;
            String[] credentials;
            while ((line = reader.readLine()) != null) {
                credentials = line.split("\\|");
                account = new Account(new ObjectId(credentials[2]),credentials[0], credentials[1]);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("No account credentials saved currently.");
        }
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
        grid.add(successText, 1, 6);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(successText.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(successText.opacityProperty(), 0.0)));
        timeline.play();
    }

    private boolean performLogin(String username, String password) {
        // Will add logic for failed login later
        String response = model.performUserRequest("GET", username, password);
        String canLogin = "Username and Password are correct.";
        if (response.contains(canLogin)) {
            String userID = response.substring(response.indexOf(canLogin) + canLogin.length());

            System.out.println("UserID " + userID + "\n" + response);
            account = new Account(new ObjectId(userID), username, password);
            return true;
        } else
            return false;
    }
    ///////////////////////////////

}
