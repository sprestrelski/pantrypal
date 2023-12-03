package code.server;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import org.bson.types.ObjectId;
import code.client.Model.*;

public class Recipe {
    private String id;
    private String userID;
    private String title;
    private String mealTag;
    private String image;
    private final List<String> ingredients = new ArrayList<>();
    private final List<String> instructions = new ArrayList<>();

    public Recipe(String id, String accountId, String title, String mealTag, String image) {
        this.id = id;
        this.userID = accountId;
        this.title = title;
        this.mealTag = mealTag;
        this.image = image;
    }

    public Recipe(String accountId, String title, String mealTag, String image) {
        this(new ObjectId().toHexString(), accountId, title, mealTag, image);
    }

    public Recipe(String accountId, String title, String mealTag) {
        this(accountId, title, mealTag, null);
        setDefaultImage();
    }

    public Recipe(String title, String mealTag) {
        this(null, title, mealTag, null);
        setDefaultImage();
    }

    public String getId() {
        return id;
    }

    public void setID(String accountID) {
        id = accountID;
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

}
