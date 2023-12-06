package code.client.Controllers;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import code.client.View.RecipeListUI;
import code.client.View.RecipeUI;
import code.client.View.View;
import code.server.AccountRequestHandler;
import code.server.IRecipeDb;
import java.net.URL;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import code.client.Model.*;
import code.client.View.AppAlert;
import code.client.View.AppFrameMic;
import code.client.View.DetailsAppFrame;
import code.server.Recipe;

public class Controller {
    // indices in sorting drop-down menu
    private final int NEWEST_TO_OLDEST_INDEX = 0;
    private final int OLDEST_TO_NEWEST_INDEX = 1;
    private final int A_TO_Z_INDEX = 2;
    private final int Z_TO_A_INDEX = 3, NONE_INDEX = 3;

    private Account account;
    private Model model;
    private View view;
    private Format format = new Format();
    private IRecipeDb recipeDb;
    private RecipeCSVWriter recipeWriter;
    private RecipeCSVReader recipeReader;
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
            goToRecipeList(true);
        }
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

    private void handleRecipePostButton(ActionEvent event) throws IOException {
        view.getDetailedView().getRefreshButton().setVisible(false);
        Recipe postedRecipe = view.getDetailedView().getDisplayedRecipe();

        view.callSaveAnimation();

        Writer writer = new StringWriter();
        recipeWriter = new RecipeCSVWriter(writer);
        recipeWriter.writeRecipe(postedRecipe);

        String recipe = writer.toString();
        Thread thread = new Thread(() -> {
                    String response = model.performRecipeRequest("POST", recipe, null);
                    // Changes UI to Detailed Recipe Screen
                    Platform.runLater(
                        () ->  {
                            
                            if (response.contains("Offline")) {
                                AppAlert.show("Connection Error", "Something went wrong. Please check your connection and try again.");
                            } else if (response.contains("Error")) {
                                AppAlert.show("Error", "Something went wrong. Please check your inputs and try again.");
                            }
                            getUserRecipeList();
                            displayUserRecipes();
                        });
                });
                thread.start();
    }

    private void handleLogOutOutButton(ActionEvent event) {
        clearCredentials();
        view.goToLoginUI();
        view.getLoginUI().getUsernameTextField().clear();
        view.getLoginUI().getPasswordField().clear();
    }

    private void handleHomeButton(ActionEvent event) {
        goToRecipeList(false);
    }

    private void goToRecipeList(boolean afterChanges) {
        if(afterChanges) {
            getUserRecipeList();
            displayUserRecipes();
        }
        view.goToRecipeList();
        addListenersToList();
        MenuButton filterMenuButton = this.view.getAppFrameHome().getFilterMenuButton();
        MenuButton sortMenuButton = this.view.getAppFrameHome().getSortMenuButton();
        view.setActiveState(filterMenuButton, 9);
        view.setActiveState(sortMenuButton, 9);

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

    private void addFilterListeners() {
        MenuButton filterMenuButton = this.view.getAppFrameHome().getFilterMenuButton();
        ObservableList<MenuItem> filterMenuItems = filterMenuButton.getItems();

        String[] filterTypes = { "breakfast", "lunch", "dinner", "none" };

        for (int i = 0; i < filterTypes.length; i++) {
            int index = i;
            filterMenuItems.get(index).setOnAction(e -> {
                filter = filterTypes[index];
                view.setActiveState(filterMenuButton, index);
                this.view.getAppFrameHome().updateDisplay(filterTypes[index]);
                addListenersToList();
            });
        }
    }

    private void addSortingListener() {
        RecipeListUI list = this.view.getAppFrameHome().getRecipeList();
        MenuButton sortMenuButton = view.getAppFrameHome().getSortMenuButton();
        ObservableList<MenuItem> sortMenuItems = sortMenuButton.getItems();
        if (list.getRecipeDB() == null) {
            return;
        }
        RecipeSorter recipeSorter = new RecipeSorter(list.getRecipeDB().getList());

        setSortAction(sortMenuItems, NEWEST_TO_OLDEST_INDEX, recipeSorter::sortNewestToOldest);
        setSortAction(sortMenuItems, OLDEST_TO_NEWEST_INDEX, recipeSorter::sortOldestToNewest);
        setSortAction(sortMenuItems, A_TO_Z_INDEX, recipeSorter::sortAToZ);
        setSortAction(sortMenuItems, Z_TO_A_INDEX, recipeSorter::sortZToA);
    }

    private void setSortAction(ObservableList<MenuItem> sortMenuItems, int index, Runnable sortAction) {
        sortMenuItems.get(index).setOnAction(e -> {
            sortAction.run();
            sortList(view.getAppFrameHome().getSortMenuButton(), index);
        });
    }

    private void sortList(MenuButton sortMenuButton, int index) {
        view.setActiveState(sortMenuButton, index);
        this.view.getAppFrameHome().updateDisplay(filter);
        addListenersToList();
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
                view.changeEditButtonColor(view.getDetailedView().getEditButton());
                handleDetailedViewListeners();
            });
        }
    }

    private void handleDetailedViewFromNewRecipeButton(ActionEvent event) {
        // Get ChatGPT response from the Model
        if (mealType != null && ingredients != null) {
            view.goToLoading();
            try {
                Thread thread = new Thread(() -> {
                    String responseText = model.performChatGPTRequest("GET", mealType, ingredients);
                    Recipe chatGPTrecipe = format.mapResponseToRecipe(mealType, responseText);
                    chatGPTrecipe.setAccountId(account.getId());
                    chatGPTrecipe.setImage(model.performDallERequest("GET", chatGPTrecipe.getTitle()));

                    // Changes UI to Detailed Recipe Screen
                    Platform.runLater(
                        () ->  {
                            view.goToDetailedView(chatGPTrecipe, false);
                            view.getDetailedView().getRecipeDetailsUI().setEditable(false);
                            handleDetailedViewListeners();
                        });
                    
                });
                thread.start();
            } catch (Exception exception) {
                AppAlert.show("Connection Error", "Something went wrong. Please check your connection and try again.");
                exception.printStackTrace();
            }
        } else

        {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        detailedView.setHomeButtonAction(this::handleHomeButton);
        detailedView.setShareButtonAction(this::handleShareButton);
        detailedView.setRefreshButtonAction(event -> {
            try {
                handleRefreshButton(event);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleEditButton(ActionEvent event) {
        Button edit = view.getDetailedView().getEditButton();
        view.getDetailedView().getRecipeDetailsUI().setEditable();
        view.changeEditButtonColor(edit);
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
        Thread thread = new Thread(() -> {
                    model.performRecipeRequest("DELETE", recipeStr, null);
                    Platform.runLater(
                        () ->  {
                            goToRecipeList(true);
                            this.view.getAppFrameHome().updateDisplay(filter); 
                            addListenersToList();
                        });
            });
            
        thread.start();
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
        view.displaySharedRecipeUI(textArea);
    }

    private void handleGoToCreateLogin(ActionEvent event) {
        view.goToCreateAcc();
    }

    private void handleGoToLogin(ActionEvent event) {
        view.goToLoginUI();
    }

    private void handleRefreshButton(ActionEvent event) throws URISyntaxException, IOException {
        // Get ChatGPT response from the Model
        if (mealType != null && ingredients != null) {
            view.goToLoading();
            try {
                Thread thread = new Thread(() -> {
                    String responseText = model.performChatGPTRequest("GET", mealType,
                            ingredients);
                    Recipe chatGPTrecipe = format.mapResponseToRecipe(mealType, responseText);
                    chatGPTrecipe.setAccountId(account.getId());
                    chatGPTrecipe.setImage(model.performDallERequest("GET",
                            chatGPTrecipe.getTitle()));

                    // Changes UI to Detailed Recipe Screen
                    Platform.runLater(
                        () ->  {
                            view.goToDetailedView(chatGPTrecipe, false);
                            view.getDetailedView().getRecipeDetailsUI().setEditable(false);
                            handleDetailedViewListeners();
                        });
                });
                thread.start();
            } catch (Exception exception) {
                AppAlert.show("Connection Error", "Something went wrong. Please check your connection and try again.");
                exception.printStackTrace();
            }
        } else {
            AppAlert.show("Input Error", "Invalid meal type or ingredients, please try again!");
        }
    }

/////////////////////////////// ACCOUNT MANAGEMENT ///////////////////////////////////
    private void handleCreateAcc(ActionEvent event) {
        GridPane grid = view.getAccountCreationUI().getRoot();
        String username = view.getAccountCreationUI().getUsernameTextField().getText();
        String password = view.getAccountCreationUI().getPasswordField().getText();

        if (username.isEmpty() || password.isEmpty()) {
            view.showErrorPane(grid, "Error. Please provide a username and password.");
            return;
        }

        view.goToLoading();

        Thread thread = new Thread(() -> {
            boolean isUsernameTaken = isUsernameTaken(username, password);
            if (view.getMainScene().equals(view.getOfflineUI())) {
            } else if (!isUsernameTaken) {
                String response = model.performAccountRequest("PUT", username, password);
                if (response.contains("Offline")) {
                    view.goToOfflineUI();
                } else {
                    view.showSuccessPane(grid);
                    view.goToLoginUI();
                }
            } else {
                view.goToCreateAcc();
                Platform.runLater(
                        () -> view.showErrorPane(grid, "Error. This username is already taken. Please choose another one."));
            }
        });
        thread.start();
    }

    private boolean isUsernameTaken(String username, String password) {
        // Check if the username is already taken
        String response = model.performAccountRequest("GET", username, password);
        if (response.contains("Offline")) {
            view.goToOfflineUI();
            return false;
        }
        return (!response.equals("Username is not found"));
    }

    private void handleLoginButton(ActionEvent event) {
        String username = view.getLoginUI().getUsernameTextField().getText();
        String password = view.getLoginUI().getPasswordField().getText();
        GridPane grid = view.getLoginUI().getRoot();
        // Perform login logic here
        if (username.isEmpty() || password.isEmpty()) {
            // Display an error message if username or password is empty
            view.showErrorPane(grid, "Error. Please provide a username and password.");
            return;
        }

        view.goToLoading();
        Thread thread = new Thread(() -> {
            boolean loginSuccessful = performLogin(username, password);
            if (view.getMainScene().equals(view.getOfflineUI())) {
            } else if (loginSuccessful) {
                Platform.runLater(
                        () -> goToRecipeList(true));
                if (!view.getLoginUI().getRememberLogin()) {
                    clearCredentials();
                } else {
                    saveCredentials(account);
                }
            } else {
                view.goToLoginUI();
                Platform.runLater(
                        () -> view.showLoginSuccessPane(grid, false));
            }
        });
        thread.start();
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

    private boolean performLogin(String username, String password) {
        // Will add logic for failed login later
        String response = model.performAccountRequest("GET", username, password);
        if (response.contains("Offline")) {
            view.goToOfflineUI();
            return false;
        } else if (response.contains("Error")) {
            return false;
        } else if (response.equals(AccountRequestHandler.USERNAME_NOT_FOUND) ||
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
    /////////////////////////////// ACCOUNT MANAGEMENT ///////////////////////////////////

    /////////////////////////////// AUDIO MANAGEMENT///////////////////////////////////
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
        } else {
            recorder.stopRecording();
            recording = false;
            mic.getRecordMealTypeButton().setStyle("");
            mic.getRecordingMealTypeLabel().setVisible(false);

            try {
                mealType = model.performWhisperRequest("GET", "mealType");
                if (mealType.contains("Error")) {
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
        } else {
            recorder.stopRecording();
            recording = false;
            mic.getRecordIngredientsButton().setStyle("");
            mic.getRecordingIngredientsLabel().setVisible(false);

            try {
                ingredients = model.performWhisperRequest("GET", "ingredients");
                String nonAsciiCharactersRegex = "[^\\x00-\\x7F]";

                if (ingredients.matches(".*" + nonAsciiCharactersRegex + ".*") ||
                        ingredients.trim().isEmpty() ||
                        ingredients.contains("you")) {
                    AppAlert.show("Input Error", "Please provide valid ingredients!");
                    ingredients = null;
                } else {
                    mic.getIngrBox().getIngredients().setText(ingredients);
                }
            } catch (IOException exception) {
                AppAlert.show("Connection Error", "Something went wrong. Please check your connection and try again.");
                exception.printStackTrace();
            }
        }
    }
    /////////////////////////////// AUDIOMANAGEMENT//////////////////////////////////

}