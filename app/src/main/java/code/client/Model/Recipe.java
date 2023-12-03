package code.client.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Comparator;
import org.bson.types.ObjectId;

public class Recipe implements Comparable<Recipe> {
    private final ObjectId id;
    private final int date;
    private String title;
    private final List<String> ingredients = new ArrayList<>();
    private final List<String> instructions = new ArrayList<>();

    public Recipe(ObjectId id, String title) {
        this.id = id;
        this.title = title;
        this.date = 0;
    }

    public Recipe(String title) {
        this(new ObjectId(), title);
    }

    public ObjectId getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getDate() {
        return date;
    }

    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }

    public void addInstruction(String instruction) {
        instructions.add(instruction);
    }

    public Iterator<String> getIngredientIterator() {
        return ingredients.iterator();
    }

    public Iterator<String> getInstructionIterator() {
        return instructions.iterator();
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Title: ").append(title).append("\n");
        strBuilder.append("Ingredients:").append("\n");
        for (String ingredient : ingredients) {
            strBuilder.append(ingredient).append("\n");
        }
        strBuilder.append("Instructions:").append("\n");
        for (String instruction : instructions) {
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

    @Override
    public int compareTo(Recipe recipe) {
        return Comparators.TITLE.compare(this, recipe);
    }
    
    // Source: https://stackoverflow.com/questions/14154127/collections-sortlistt-comparator-super-t-method-example
    public static class Comparators {
        
        public static final Comparator<Recipe> TITLE = 
            (Recipe r1, Recipe r2) -> r1.getTitle().compareTo(r2.getTitle());

        public static final Comparator<Recipe> DATE = 
            (Recipe r1, Recipe r2) -> Integer.compare(r1.getDate(), r2.getDate());
        
        // public static Comparator<Recipe> TITLE = new Comparator<>() {
        //     @Override
        //     public int compare(Recipe r1, Recipe r2) {
        //         return r1.getTitle().compareTo(r2.getTitle());
        //     }
        // };

        // public static Comparator<Recipe> DATE = new Comparator<>() {
        //     @Override
        //     public int compare(Recipe r1, Recipe r2) {
        //         return r1.getDate() - r2.getDate();
        //     }
        // };
    }
}
