package code.client.View;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import java.util.Iterator;

import code.server.Recipe;

public class RecipeDetailsUI extends HBox {
    private TextField titleTextField;
    private TextArea ingredientTextArea;
    private TextArea instructionTextArea;
    private Recipe recipe;
    private boolean editableStatus = false;

    public RecipeDetailsUI(Recipe recipe) {
        titleTextField = new TextField();
        ingredientTextArea = new TextArea();
        instructionTextArea = new TextArea();
        titleTextField.setEditable(false);
        ingredientTextArea.setEditable(false);
        instructionTextArea.setEditable(false);

        titleTextField.setText(recipe.getTitle());
        this.recipe = recipe;

        StringBuilder ingredientBuilder = new StringBuilder();
        Iterator<String> ingredientIterator = recipe.getIngredientIterator();
        while (ingredientIterator.hasNext()) {
            ingredientBuilder.append(ingredientIterator.next() + "\n");
        }
        ingredientTextArea.setText(ingredientBuilder.toString());

        StringBuilder instructionBuilder = new StringBuilder();
        Iterator<String> instructionIterator = recipe.getInstructionIterator();
        while (instructionIterator.hasNext()) {
            instructionBuilder.append(instructionIterator.next() + "\n");
        }
        instructionTextArea.setText(instructionBuilder.toString());

        // getChildren().add(titleTextField);
        getChildren().add(ingredientTextArea);
        getChildren().add(instructionTextArea);
    }

    public TextField getTitleField() {
        return titleTextField;
    }

    public TextArea getIngredientsField() {
        return ingredientTextArea;
    }

    public TextArea getInstructionsField() {
        return instructionTextArea;
    }

    public boolean isEditable() {
        return editableStatus;
    }
    public void setEditable(boolean value) {
        editableStatus = value;
    }

    public void setEditable() {
        // titleTextField.setEditable(!editableStatus); // TODO : maybe later?
        ingredientTextArea.setEditable(!editableStatus);
        instructionTextArea.setEditable(!editableStatus);
        editableStatus = !editableStatus;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }
}
