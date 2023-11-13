package code.client.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Recipe {
    private UUID id;
    private String title;
    private final List<String> ingredientList = new ArrayList<>();
    private final List<String> instructionList = new ArrayList<>();

    public Recipe(String title) {
        this.id = UUID.nameUUIDFromBytes(title.getBytes());
        this.title = title;
    }

    public void setID(UUID uuid) {
        id = uuid;
    }

    public void setTitle(String name) {
        title = name;
    }

    public UUID getId() {
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

    public String getHttpString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(title).append("| ");
        for (String ingredient : ingredientList) {
            strBuilder.append(ingredient).append(";;");
        }
        strBuilder.append("| ");
        for (String instruction : instructionList) {
            strBuilder.append(instruction).append(";;");
        }
        return strBuilder.toString();
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Recipe recipe)) {
            return false;
        }
        return id.equals(recipe.id);
    }
}
