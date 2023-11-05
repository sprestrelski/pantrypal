package code.client.Model;

import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Recipe {
    private TextField name;

    private TextField recipeDetails;
    
    Recipe() {
        Font biggerFont = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 22);
        Font smallerFont = Font.font("georgia", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 18);

        name = new TextField();
        name.setPromptText("Recipe Name");
        name.setFont(biggerFont);

        recipeDetails = new TextField();
        recipeDetails.setFont(smallerFont);

    }

    public String getRecipeName() {
        return this.name.getText();
    }

    public String getRecipeDetails() {
        return this.recipeDetails.getText();
    }

    public void setRecipeName(String name) {
        this.name.setText(name);
    }

    public void setRecipeDetails(String details) {
        this.recipeDetails.setText(details);
    }
}
