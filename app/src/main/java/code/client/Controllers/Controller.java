package code.client.Controllers;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import code.client.View.RecipeListUI;
import code.client.View.RecipeUI;
import code.client.View.View;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.util.Duration;
import code.client.Model.*;
import code.client.View.AppAlert;

public class Controller {
    private Model model;
    private View view;
    private Recipe recipe;
    private RecipeCSVWriter recipeWriter;
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
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private void handlePostButton(ActionEvent event) throws IOException {
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
        // Testing
        inputs.set(0, "w");
        inputs.set(1, "s");
        // Testing
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
                handlePostButton(event);
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
}
