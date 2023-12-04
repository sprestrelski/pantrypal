package code.client.View;

import javafx.scene.control.Button;
import code.server.Recipe;

public class MealTagStyler {
    public static void styleTags(Recipe recipe, Button mealType) {
        switch (recipe.getMealTag().toLowerCase()) {
            case "breakfast":
                mealType.setStyle(
                        "-fx-text-fill: black; -fx-font: 12 arial; -fx-font-weight: bold; -fx-background-color: #FF7276; -fx-border-width: 0; -fx-background-radius: 150;  -fx-pref-width: 100;  -fx-pref-height: 50;");
                mealType.setText("Breakfast");
                break;

            case "lunch":
                mealType.setStyle(
                        "-fx-text-fill: black; -fx-font: 12 arial; -fx-font-weight: bold; -fx-background-color: #00FFFF; -fx-border-width: 0; -fx-background-radius: 150;  -fx-pref-width: 100;  -fx-pref-height: 50;");
                mealType.setText("Lunch");
                break;

            case "dinner":
                mealType.setStyle(
                        "-fx-text-fill: black; -fx-font: 12 arial; -fx-font-weight: bold; -fx-background-color: #00FF00; -fx-border-width: 0; -fx-background-radius: 150;  -fx-pref-width: 100;  -fx-pref-height: 50;");
                mealType.setText("Dinner");
                break;
        }
    }
}
