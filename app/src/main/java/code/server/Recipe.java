package code.server;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Iterator;

import org.bson.types.ObjectId;

import java.util.*;

import code.AppConfig;

public class Recipe implements Comparable<Recipe> {
    private String id;
    private String userID;
    private String title;
    private String mealTag;
    private long date;
    private String image;
    private final List<String> ingredients = new ArrayList<>();
    private final List<String> instructions = new ArrayList<>();

    public Recipe(String id, String accountId, String title, String mealTag, long date, String image) {
        this.id = id;
        this.userID = accountId;
        this.title = title;
        this.mealTag = mealTag;
        this.date = date;
        this.image = image;
    }

    // public Recipe(String accountId, String title, String mealTag, String image) {
    //     this(new ObjectId().toHexString(), accountId, title, mealTag, image);
    // }

    // public Recipe(String accountId, String title, String mealTag) {
    //     this(accountId, title, mealTag, null);
    //     setDefaultImage();
    // }

    // Keep this for one testing purposes please :)
    public Recipe(String title, String mealTag) {
        this(new ObjectId().toHexString(), new ObjectId().toHexString(), title, mealTag, 1, "");
        setDefaultImage();
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAccountId(String accountId) {
        this.userID = accountId;
    }

    public String getAccountId() {
        return userID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setMealTag(String mealTag) {
        this.mealTag = mealTag;
    }

    public String getMealTag() {
        return mealTag;
    }

    public void setDefaultImage() {
        File file = new File(AppConfig.RECIPE_IMG_FILE);
        try {
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            this.image = Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception fileError) {
            fileError.printStackTrace();
        }
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
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
        strBuilder.append("Meal tag: ").append(mealTag).append("\n");
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
        return title.equals(recipe.getTitle()) &&
                mealTag.equals(recipe.mealTag) &&
                ingredients.equals(recipe.ingredients) &&
                instructions.equals(recipe.instructions);
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
            (Recipe r1, Recipe r2) -> Long.compare(r1.getDate(), r2.getDate()); 
        
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
