package code.client.View;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.Iterator;

import code.client.Model.Recipe;

public class RecipeDetailsUI extends HBox {
    private TextField titleTextField;
    private TextArea ingredientTextArea;
    private TextArea instructionTextArea;
    private Recipe recipe;

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

    public void setEditable(boolean status) {
        titleTextField.setEditable(status);
        ingredientTextArea.setEditable(status);
        instructionTextArea.setEditable(status);
    }

    public Recipe getRecipe() {
        return this.recipe;
    }
}
