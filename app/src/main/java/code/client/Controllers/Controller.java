package code.client.Controllers;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;

import code.client.View.RecipeListUI;
import code.client.View.RecipeUI;
import code.client.View.View;
import code.server.AccountRequestHandler;
import code.server.IRecipeDb;

import java.net.URL;
import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Duration;
import code.client.Model.*;
import code.client.View.AppAlert;
import code.client.View.AppFrameHome;
import code.client.View.AppFrameMic;
import code.client.View.DetailsAppFrame;
import code.server.Recipe;

public class Controller {
    // Index of newest to oldest in sorting drop down menu, index of breakfast in
    // filtering drop down menu
    private final int NEWEST_TO_OLDEST_INDEX = 0, BREAKFAST_INDEX = 0;
    // Index of oldest to newest in sorting drop down menu, index of lunch in
    // filtering drop down menu
    private final int OLDEST_TO_NEWEST_INDEX = 1, LUNCH_INDEX = 1;
    // Index of A to Z in sorting drop down menu, index of dinner in filtering drop
    // down menu
    private final int A_TO_Z_INDEX = 2, DINNER_INDEX = 2;
    // Index of Z to A in sorting drop down menu, index of none in filtering drop
    // down menu
    private final int Z_TO_A_INDEX = 3, NONE_INDEX = 3;

    private Account account;
    private Model model;
    private View view;
    private Format format = new Format();
    private IRecipeDb recipeDb;
    private RecipeCSVWriter recipeWriter;
    private RecipeCSVReader recipeReader;
    private String title;
    private String defaultButtonStyle, onStyle, offStyle, blinkStyle;
    private String filter;

    // Audio Stuff
    private boolean recording; // keeps track if the app is currently recording
    private final AppAudioRecorder recorder = new AppAudioRecorder();
    private String mealType; // stores the meal type specified by the user
    private String ingredients; // stores the ingredients listed out by the user

    public Controller(View view, Model model) {

        this.view = view;
        this.model = model;
        filter = "none";
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
        this.view.getAccountCreationUI().setGoToLoginAction(this::handleGoToLogin);
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
        Date currTime = new Date();
        postedRecipe.setDate(currTime.getTime());

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
        MenuButton filterMenuButton = this.view.getAppFrameHome().getFilterMenuButton();
        MenuButton sortMenuButton = this.view.getAppFrameHome().getSortMenuButton();
        setActiveState(filterMenuButton, 9);
        setActiveState(sortMenuButton, 9);

        RecipeListUI recipeListUI = this.view.getAppFrameHome().getRecipeList();
        RecipeSorter recipeSorter = new RecipeSorter(recipeListUI.getRecipeDB().getList());
        recipeSorter.sortNewestToOldest();
        sortList(sortMenuButton, NEWEST_TO_OLDEST_INDEX);
        // 9 is beyond the scope of menuItem indexes
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
        recipeListUI.update(filter);
    }

    private void handleNewButton(ActionEvent event) throws URISyntaxException, IOException {
        view.goToAudioCapture();
        AppFrameMic mic = this.view.getAppFrameMic();
        mic.setGoToDetailedButtonAction(this::handleDetailedViewFromNewRecipeButton);
        mic.setGoToHomeButtonAction(this::handleHomeButton);
        mic.setRecordIngredientsButtonAction(this::handleRecordIngredients);
        mic.setRecordMealTypeButtonAction(event1 -> {
            try {
                handleRecordMealType(event1);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

    }

    private void addFilterListeners() {
        MenuButton filterMenuButton = this.view.getAppFrameHome().getFilterMenuButton();
        ObservableList<MenuItem> filterMenuItems = filterMenuButton.getItems();

        // Filter to show only breakfast recipes when criteria is selected
        filterMenuItems.get(BREAKFAST_INDEX).setOnAction(e -> {
            filter = "breakfast";
            setActiveState(filterMenuButton, BREAKFAST_INDEX);
            this.view.getAppFrameHome().updateDisplay("breakfast");
            addListenersToList();
        });
        // Filter to show only lunch recipes when criteria is selected
        filterMenuItems.get(LUNCH_INDEX).setOnAction(e -> {
            filter = "lunch";
            setActiveState(filterMenuButton, LUNCH_INDEX);
            this.view.getAppFrameHome().updateDisplay("lunch");
            addListenersToList();
        });
        // Filter to show only dinner recipes when criteria is selected
        filterMenuItems.get(DINNER_INDEX).setOnAction(e -> {
            filter = "dinner";
            setActiveState(filterMenuButton, DINNER_INDEX);
            this.view.getAppFrameHome().updateDisplay("dinner");
            addListenersToList();
        });
        // Remove selected filter to show all recipes
        filterMenuItems.get(NONE_INDEX).setOnAction(e -> {
            filter = "none";
            setActiveState(filterMenuButton, NONE_INDEX);
            this.view.getAppFrameHome().updateDisplay("none");
            addListenersToList();
        });
    }

    private void addSortingListener() {
        RecipeListUI list = this.view.getAppFrameHome().getRecipeList();
        MenuButton sortMenuButton = view.getAppFrameHome().getSortMenuButton();
        ObservableList<MenuItem> sortMenuItems = sortMenuButton.getItems();
        if (list.getRecipeDB() == null) {
            return;
        }
        RecipeSorter recipeSorter = new RecipeSorter(list.getRecipeDB().getList());

        // Setting action for newest to oldest sorting criteria
        sortMenuItems.get(NEWEST_TO_OLDEST_INDEX).setOnAction(e -> {
            recipeSorter.sortNewestToOldest();
            sortList(sortMenuButton, NEWEST_TO_OLDEST_INDEX);
        });
        // Setting action for oldest to newest sorting criteria
        sortMenuItems.get(OLDEST_TO_NEWEST_INDEX).setOnAction(e -> {
            recipeSorter.sortOldestToNewest();
            sortList(sortMenuButton, OLDEST_TO_NEWEST_INDEX);
        });
        // Setting action for A to Z sorting criteria
        sortMenuItems.get(A_TO_Z_INDEX).setOnAction(e -> {
            recipeSorter.sortAToZ();
            sortList(sortMenuButton, A_TO_Z_INDEX);
        });
        // Setting action for Z to A sorting criteria
        sortMenuItems.get(Z_TO_A_INDEX).setOnAction(e -> {
            recipeSorter.sortZToA();
            sortList(sortMenuButton, Z_TO_A_INDEX);
        });
    }

    private void sortList(MenuButton sortMenuButton, int index) {
        setActiveState(sortMenuButton, index);
        this.view.getAppFrameHome().updateDisplay(filter);
        addListenersToList();
    }

    private void setActiveState(MenuButton items, int index) {
        for (int i = 0; i < NONE_INDEX + 1; i++) {
            if (i == index) {
                items.getItems().get(i).setStyle("-fx-background-color: #90EE90");
            } else {
                items.getItems().get(i).setStyle("-fx-background-color: transparent;");
            }
        }
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
        addSortingListener();
        addFilterListeners();
        RecipeListUI list = view.getAppFrameHome().getRecipeList();
        for (int i = 0; i < list.getChildren().size(); i++) {
            RecipeUI currRecipe = (RecipeUI) list.getChildren().get(i);
            currRecipe.getDeleteButton().setOnAction(e -> {
                try {
                    deleteGivenRecipe(currRecipe.getRecipe());
                } catch (IOException e1) {
                    System.out.println("Could not delete recipe with id:title of " + currRecipe.getRecipe().getId()
                            + ":" + currRecipe.getRecipe().getTitle());
                    e1.printStackTrace();
                }
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
        if (mealType != null && ingredients != null) {
            try {
                String responseText = model.performChatGPTRequest("GET", mealType, ingredients);
                Recipe chatGPTrecipe = format.mapResponseToRecipe(mealType, responseText);
                chatGPTrecipe.setAccountId(account.getId());
                chatGPTrecipe.setImage(model.performDallERequest("GET", chatGPTrecipe.getTitle()));

                // Changes UI to Detailed Recipe Screen
                view.goToDetailedView(chatGPTrecipe, false);
                view.getDetailedView().getRecipeDetailsUI().setEditable(false);
                handleDetailedViewListeners();

            } catch (Exception exception) {
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
                detailedView.getShareButton().setVisible(true);
                detailedView.getDeleteButton().setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        detailedView.setEditButtonAction(this::handleEditButton);
        detailedView.setDeleteButtonAction(event -> {
            try {
                handleDeleteButton(event);
                goToRecipeList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        detailedView.setHomeButtonAction(this::handleHomeButton);
        detailedView.setShareButtonAction(this::handleShareButton);
        detailedView.setRefreshButtonAction(this::handleRefreshButton);
    }

    private void handleEditButton(ActionEvent event) {
        Button edit = view.getDetailedView().getEditButton();
        view.getDetailedView().getRecipeDetailsUI().setEditable();
        changeEditButtonColor(edit);
    }

    private void handleDeleteButton(ActionEvent event) throws IOException {
        DetailsAppFrame detailsAppFrame = this.view.getDetailedView();
        Recipe displayed = detailsAppFrame.getDisplayedRecipe();
        deleteGivenRecipe(displayed);

    }

    private void deleteGivenRecipe(Recipe recipe) throws IOException {
        Writer writer = new StringWriter();
        recipeWriter = new RecipeCSVWriter(writer);
        recipeWriter.writeRecipe(recipe);

        String recipeStr = writer.toString();

        System.out.println("Deleting id: " + recipe.getId());
        model.performRecipeRequest("DELETE", recipeStr, null);
        this.view.getAppFrameHome().updateDisplay(filter);
    }

    private void handleShareButton(ActionEvent event) {
        Recipe shownRecipe = this.view.getDetailedView().getDisplayedRecipe();
        String id = shownRecipe.getId();
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
        showShareRecipe(textArea);
    }

    private void showShareRecipe(Hyperlink textArea) {
        String styleAlert = "-fx-background-color: #F1FFCB; -fx-font-weight: bold; -fx-font: 14 arial";

        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(textArea, 0, 0);
        gridPane.setStyle(styleAlert);
        gridPane.setPrefSize(220, 220);
        gridPane.setAlignment(Pos.TOP_CENTER);
        textArea.setTextAlignment(TextAlignment.CENTER);
        Button copyButton = new Button("Copy to Clipboard");
        copyButton.setOnAction(event -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(textArea.getText());
            clipboard.setContent(content);
        });
        gridPane.add(copyButton, 0, 3);

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

    private void handleGoToLogin(ActionEvent event) {
        view.goToLoginUI();
    }

    private void handleRefreshButton(ActionEvent event) {
        // Get ChatGPT response from the Model
        if (mealType != null && ingredients != null) {
            try {
                String responseText = model.performChatGPTRequest("GET", mealType, ingredients);
                Recipe chatGPTrecipe = format.mapResponseToRecipe(mealType, responseText);
                chatGPTrecipe.setAccountId(account.getId());
                chatGPTrecipe.setImage(model.performDallERequest("GET", chatGPTrecipe.getTitle()));

                // Changes UI to Detailed Recipe Screen
                view.getDetailedView().setRecipe(chatGPTrecipe);

            } catch (Exception exception) {
                AppAlert.show("Connection Error", "Something went wrong. Please check your connection and try again.");
                exception.printStackTrace();
            }
        } else {
            AppAlert.show("Input Error", "Invalid meal type or ingredients, please try again!");
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
        } else if (isUsernameTaken(username, password)) {
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

    private boolean isUsernameTaken(String username, String password) {
        // Check if the username is already taken
        // temporary logic, no database yet
        String response = model.performAccountRequest("GET", username, password);
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

    /////////////////////////////// AUDIOMANAGEMENT///////////////////////////////////
    public void handleRecordMealType(ActionEvent event) throws IOException, URISyntaxException {
        recordMealType();
    }

    public void handleRecordIngredients(ActionEvent event) {
        recordIngredients();
    };

    private void recordMealType() {
        AppFrameMic mic = this.view.getAppFrameMic();
        if (!recording) {
            recorder.startRecording();
            recording = true;
            mic.getRecordMealTypeButton().setStyle("-fx-background-color: #FF0000;");
            mic.getRecordingMealTypeLabel().setVisible(true);
            // recordingLabel1.setStyle("-fx-font-color: #FF0000;");
        } else {
            recorder.stopRecording();
            recording = false;
            mic.getRecordMealTypeButton().setStyle("");
            mic.getRecordingMealTypeLabel().setVisible(false);
            // recordingLabel1.setStyle("");

            try {
                mealType = model.performWhisperRequest("GET", "mealType");
                if (mealType == null) {
                    AppAlert.show("Input Error", "Please say a valid meal type!");
                }
                mic.getMealBox().getMealType().setText(mealType);
            } catch (IOException exception) {
                AppAlert.show("Connection Error", "Something went wrong. Please check your connection and try again.");
                exception.printStackTrace();
            }
        }
    }

    private void recordIngredients() {
        AppFrameMic mic = this.view.getAppFrameMic();
        if (!recording) {
            recorder.startRecording();
            recording = true;
            mic.getRecordIngredientsButton().setStyle("-fx-background-color: #FF0000;");
            mic.getRecordingIngredientsLabel().setVisible(true);
            // recordingLabel2.setStyle("-fx-background-color: #FF0000;");
        } else {
            recorder.stopRecording();
            recording = false;
            mic.getRecordIngredientsButton().setStyle("");
            mic.getRecordingIngredientsLabel().setVisible(false);
            // recordingLabel2.setStyle("");

            try {
                mealType = model.performWhisperRequest("GET", "ingredients");
                String nonAsciiCharactersRegex = "[^\\x00-\\x7F]";

                if (ingredients.matches(".*" + nonAsciiCharactersRegex + ".*") ||
                        ingredients.trim().isEmpty() ||
                        ingredients.contains("you")) {
                    AppAlert.show("Input Error", "Please provide valid ingredients!");
                    ingredients = null;
                } else {
                    mic.getIngredBox().getIngredients().setText(ingredients);
                }
            } catch (IOException exception) {
                AppAlert.show("Connection Error", "Something went wrong. Please check your connection and try again.");
                exception.printStackTrace();
            }
        }
    }

    /////////////////////////////// AUDIOMANAGEMENT//////////////////////////////////

}