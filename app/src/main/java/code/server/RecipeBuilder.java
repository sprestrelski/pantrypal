package code.server;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

import org.bson.types.ObjectId;

import code.client.Model.AppConfig;

public class RecipeBuilder {
    private Recipe recipe;

    public RecipeBuilder(String userID, String title) {
        recipe = new Recipe(new ObjectId().toHexString(),
                            userID,
                            title, 
                            "",
                            0,
                            getDefaultImage());
    }

    public RecipeBuilder setId(String id) {
        recipe.setId(id);
        return this;
    }

    public RecipeBuilder setAccountId(String accountId) {
        recipe.setAccountId(accountId);
        return this;
    }

    public RecipeBuilder setTitle(String title) {
        recipe.setTitle(title);
        return this;
    }

    public RecipeBuilder setMealTag(String mealTag) {
        recipe.setMealTag(mealTag);
        return this;
    }

    public RecipeBuilder setDate(long date) {
        recipe.setDate(date);
        return this;
    }

    public RecipeBuilder setImage(String image) {
        recipe.setImage(image);
        return this;
    }

    private String getDefaultImage() {
        String image = "";
        File file = new File(AppConfig.RECIPE_IMG_FILE);
        
        try {
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            image = Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception fileError) {
            fileError.printStackTrace();
        }
        
        return image;
    }

    public Recipe buildRecipe() {
        return recipe;
    }
}
