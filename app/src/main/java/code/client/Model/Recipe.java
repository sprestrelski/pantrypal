package code.client.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Recipe {
    private final String id;
    private final String title;
    private final List<String> ingredientList = new ArrayList<>();
    private final List<String> instructionList = new ArrayList<>();

    public Recipe(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void addIngredient(String ingredient) {
        ingredientList.add(ingredient);
    }

    public void addInstruction(String instruction) {
        instructionList.add(instruction);
    }

    public Iterator<String> getIngredientIterator() {
        return ingredientList.iterator();
    }

    public Iterator<String> getInstructionIterator() {
        return instructionList.iterator();
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Title: ").append(title).append("\n");
        strBuilder.append("Ingredients:\n");
        for (String ingredient : ingredientList) {
            strBuilder.append(ingredient).append("\n");
        }
        strBuilder.append("Instructions:\n");
        for (String instruction : instructionList) {
            strBuilder.append(instruction).append("\n");
        }
        return strBuilder.toString();
    }
}
