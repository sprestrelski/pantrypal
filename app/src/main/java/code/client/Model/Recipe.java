package code.client.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bson.types.ObjectId;

public class Recipe {
    private final ObjectId id;
    private ObjectId accountId;
    private String title;
    private String mealTag;
    private String image;
    private final List<String> ingredients = new ArrayList<>();
    private final List<String> instructions = new ArrayList<>();

    public Recipe(ObjectId id, ObjectId accountId, String title, String mealTag, String image) {
        this.id = id;
        this.accountId = accountId;
        this.title = title;
        this.mealTag = mealTag;
        this.image = image;
    }

    public Recipe(ObjectId accountId, String title, String mealTag, String image) {
        this(new ObjectId(), accountId, title, mealTag, image);
    }

    public Recipe(ObjectId accountId, String title, String mealTag) {
        this(accountId, title, mealTag, null);
    }

    public Recipe(String title, String mealTag) {
        this(null, title, mealTag, null);
    }

    public ObjectId getId() {
        return id;
    }

    public void setAccountId(ObjectId accountId) {
        this.accountId = accountId;
    }

    public ObjectId getAccountId() {
        return accountId;
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
        return id.equals(recipe.id);
    }
}
