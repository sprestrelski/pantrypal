package code.client.View;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.Iterator;

import code.client.Model.Recipe;

public class RecipeDetailsUI extends HBox {
    private final TextField titleTextField;
    private final TextArea ingredientTextArea;
    private final TextArea instructionTextArea;

    public RecipeDetailsUI(Recipe recipe) {
        titleTextField = new TextField();
        ingredientTextArea = new TextArea();
        instructionTextArea = new TextArea();

        titleTextField.setText(recipe.getTitle());

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
}
