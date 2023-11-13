package code.client.Controllers;

import java.io.*;
import java.util.UUID;

import code.client.View.DetailsAppFrame;
import code.client.View.HomeScreen;
import javafx.event.ActionEvent;
import code.client.Model.*;

public class Controller {
    private Model model;
    private HomeScreen home;
    private DetailsAppFrame details;
    private Recipe recipe;
    private RecipeWriter recipeWriter;
    private String title;

    public Controller(HomeScreen home, DetailsAppFrame details, Model model) {

        this.home = home;
        this.details = details;
        this.model = model;

        // recipe buttons
        this.details.setPostButtonAction(event -> {
            try {
                handlePostButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.home.getAppFrameHome().setGetButtonAction(this::handleGetButton);
        this.details.setPutButtonAction(event -> {
            try {
                handlePutButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.details.setDeleteButtonAction(this::handleDeleteButton);
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private void handlePostButton(ActionEvent event) throws IOException {
        Writer writer = new StringWriter();
        recipeWriter = new RecipeWriter(writer);
        recipeWriter.writeRecipe(recipe);

        String recipe = writer.toString();
        model.performRequest("POST", recipe, null);
    }

    private void handleGetButton(ActionEvent event) {
        String uuid = UUID.fromString(title).toString();
        model.performRequest("GET", null, uuid);
    }

    private void handlePutButton(ActionEvent event) throws IOException {
        Writer writer = new StringWriter();
        recipeWriter = new RecipeWriter(writer);
        recipeWriter.writeRecipe(recipe);

        String recipe = writer.toString();
        model.performRequest("POST", recipe, null);
    }

    private void handleDeleteButton(ActionEvent event) {
        String uuid = UUID.fromString(title).toString();
        model.performRequest("DELETE", null, uuid);
    }

}
