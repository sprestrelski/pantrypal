package code.client.Controllers;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import code.client.View.RecipeListUI;
import code.client.View.RecipeUI;
import code.client.View.View;
import code.server.AccountRequestHandler;
import code.server.IRecipeDb;

import java.net.URL;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
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
import code.client.View.DetailsAppFrame;
import code.server.Recipe;

public class Controller {
    private Account account;
    private Model model;
    private View view;
    private IRecipeDb recipeDb;
    private RecipeCSVWriter recipeWriter;
    private RecipeCSVReader recipeReader;
    private String title;
    private String defaultButtonStyle, onStyle, offStyle, blinkStyle;

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
        if (account != null) {
            this.view.getLoginUI().setLoginCreds(account);
            goToRecipeList();
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
        // System.out.println("Posting: " + recipe);

        model.performRecipeRequest("POST", recipe, null);
    }

    private void goToRecipeList() {
        getUserRecipeList();
        displayUserRecipes();
        view.goToRecipeList();
        addListenersToList();
    }

    private void getUserRecipeList() {
        String userID = account.getId();
        String response = model.performRecipeRequest("GET", null, userID);
        Reader reader = new StringReader(response);
        recipeReader = new RecipeCSVReader(reader);

        try {
            recipeDb = new RecipeListDb();
            recipeReader.readRecipeDb(recipeDb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayUserRecipes() {
        RecipeListUI recipeListUI = this.view.getAppFrameHome().getRecipeList();
        recipeListUI.setRecipeDB(recipeDb);
        recipeListUI.update();
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
        goToRecipeList();
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
            RecipeToImage imageCaller = new MockDallEService(); // new DallEService();

            try {
                String audioOutput1 = mealType;
                String audioOutput2 = ingredients;// audio.processAudio();
                String responseText = caller.getResponse(audioOutput1, audioOutput2);
                Recipe chatGPTrecipe = caller.mapResponseToRecipe(mealType, responseText);
                chatGPTrecipe.setAccountId(account.getId());
                chatGPTrecipe.setImage(imageCaller.getResponse(chatGPTrecipe.getTitle()));

                // Changes UI to Detailed Recipe Screen
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
        DetailsAppFrame detailedView = this.view.getDetailedView();
        detailedView.setPostButtonAction(event -> {
            try {
                handleRecipePostButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        detailedView.setEditButtonAction(this::handleEditButton);
        detailedView.setDeleteButtonAction(this::handleDeleteButton);
        detailedView.setHomeButtonAction(this::handleHomeButton);
        detailedView.setShareButtonAction(this::handleShareButton);
        detailedView.setRefreshButtonAction(this::handleRefreshButton);
    }

    private void handleEditButton(ActionEvent event) {
        Button edit = view.getDetailedView().getEditButton();
        view.getDetailedView().getRecipeDetailsUI().setEditable();
        changeEditButtonColor(edit);
    }

    private void handleDeleteButton(ActionEvent event) {
        String uuid = UUID.fromString(title).toString();
        model.performRecipeRequest("DELETE", null, uuid);
    }

    private void handleShareButton(ActionEvent event) {
        Recipe shownRecipe = this.view.getDetailedView().getDisplayedRecipe();
        String id = shownRecipe.getId();

        String styleAlert = "-fx-background-color: #F1FFCB; -fx-font-weight: bold;";
        Hyperlink textArea = new Hyperlink(AppConfig.SHARE_LINK + account.getUsername() + "/" + id);
        textArea.setOnAction(action -> {
            try {
                java.awt.Desktop.getDesktop() // Format: localhost:8100/recipes/username/recipeID
                        .browse(new URL(AppConfig.SHARE_LINK + account.getUsername() + "/" + id).toURI());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
        textArea.setWrapText(true);
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(textArea, 0, 0);
        gridPane.setStyle(styleAlert);
        gridPane.setPrefSize(220, 220);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Share this recipe!");
        alert.setHeaderText("Share this recipe with a friend!");
        alert.getDialogPane().setContent(gridPane);
        alert.showAndWait();
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

    private void handleRefreshButton(ActionEvent event) {
        // Get ChatGPT response from the Model
        List<String> inputs = view.getAppFrameMic().getVoiceResponse();

        String mealType = inputs.get(0);
        String ingredients = inputs.get(1);

        TextToRecipe caller = new MockGPTService();// new ChatGPTService();
        RecipeToImage imageCaller = new MockDallEService(); // new DallEService();

        try {
            String audioOutput1 = mealType;
            String audioOutput2 = ingredients;// audio.processAudio();
            String responseText = caller.getResponse(audioOutput1, audioOutput2);
            Recipe chatGPTrecipe = caller.mapResponseToRecipe(mealType, responseText);
            chatGPTrecipe.setAccountId(account.getId());
            chatGPTrecipe.setImage(imageCaller.getResponse(chatGPTrecipe.getTitle()));

            // Changes UI to Detailed Recipe Screen
            view.getDetailedView().setRecipe(chatGPTrecipe);
        } catch (IOException | URISyntaxException | InterruptedException exception) {
            AppAlert.show("Connection Error", "Something went wrong. Please check your connection and try again.");
            exception.printStackTrace();
        }
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
            model.performAccountRequest("PUT", username, password);
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
        String response = model.performAccountRequest("GET", username, "");
        // System.out.println("Response for usernameTaken : " + response);
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

                goToRecipeList();

                if (!view.getLoginUI().getRememberLogin()) {
                    clearCredentials();
                } else {
                    saveCredentials(account);
                }
            } else {
                showLoginSuccessPane(grid, false);
            }
        }
    }

    private void clearCredentials() {
        try (FileWriter writer = new FileWriter(AppConfig.CREDENTIALS_CSV_FILE, false)) {
            writer.write("");
            writer.flush();
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            System.out.println("Account credentials could not be erased.");
        }
    }

    private void saveCredentials(Account acc) {
        try (FileWriter writer = new FileWriter(AppConfig.CREDENTIALS_CSV_FILE, true)) {
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
            BufferedReader reader = new BufferedReader(new FileReader(AppConfig.CREDENTIALS_CSV_FILE));
            String line;
            String[] credentials;
            while ((line = reader.readLine()) != null) {
                credentials = line.split("\\|");
                account = new Account(credentials[2], credentials[0], credentials[1]);
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
        String response = model.performAccountRequest("GET", username, password);
        if (response.equals(AccountRequestHandler.USERNAME_NOT_FOUND) ||
                response.equals(AccountRequestHandler.INCORRECT_PASSWORD) ||
                response.equals(AccountRequestHandler.TAKEN_USERNAME)) {
            return false;
        }
        // The response is the account id
        String accountId = response;
        // System.out.println("Account ID " + accountId + "\n" + response);
        account = new Account(accountId, username, password);
        return true;
    }
    ///////////////////////////////
}