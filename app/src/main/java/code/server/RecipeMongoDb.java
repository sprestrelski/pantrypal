package code.server;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;
import java.util.List;
import java.util.ArrayList;

public class RecipeMongoDb implements IRecipeDb {
    private MongoCollection<Document> recipeDocumentCollection;

    public RecipeMongoDb(MongoCollection<Document> recipeDocumentCollection) {
        this.recipeDocumentCollection = recipeDocumentCollection;
    }

    private Recipe jsonToRecipe(Document recipeDocument) {
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(recipeDocument.toJson(), Recipe.class);
        JsonObject jsonObj = JsonParser.parseString(recipeDocument.toJson().toString()).getAsJsonObject();
        String recipeId = jsonObj.getAsJsonObject("_id").get("$oid").getAsString();
        recipe.setId(recipeId);
        // System.out.println(recipe.getId());
        return recipe;
    }

    // Possible refactoring to be one method instead of two separate ??
    @Override
    public List<Recipe> getList() {
        Recipe recipe;
        Document recipeDocument;
        var recipeDocumentCursor = recipeDocumentCollection.find().cursor();
        List<Recipe> recipeList = new ArrayList<>();

        while (recipeDocumentCursor.hasNext()) {
            recipeDocument = recipeDocumentCursor.next();
            recipe = jsonToRecipe(recipeDocument);
            recipeList.add(recipe);
        }

        return recipeList;
    }

    @Override
    public List<Recipe> getList(String accountId) {
        Recipe recipe;
        Document recipeDocument;
        Bson filter = eq("userID", accountId);
        var recipeDocumentCursor = recipeDocumentCollection.find(filter).cursor();
        List<Recipe> recipeList = new ArrayList<>();

        while (recipeDocumentCursor.hasNext()) {
            recipeDocument = recipeDocumentCursor.next();
            recipe = jsonToRecipe(recipeDocument);
            recipeList.add(recipe);
        }
        return recipeList;
    }

    @Override
    public boolean add(Recipe recipe) {
        Bson filter = eq("_id", new ObjectId(recipe.getId()));
        List<Bson> updates = new ArrayList<>();
        Bson updateUserId = set("userID", recipe.getAccountId());
        Bson updateTitle = set("title", recipe.getTitle());
        Bson updateMealTag = set("mealTag", recipe.getMealTag());
        Bson updateIngr = set("ingredients", Lists.newArrayList(recipe.getIngredientIterator()));
        Bson updateInstr = set("instructions", Lists.newArrayList(recipe.getInstructionIterator()));
        Bson updateDate = set("date", recipe.getDate());
        Bson updateImage = set("image", recipe.getImage());
        updates.addAll(Arrays.asList(updateUserId, 
                                    updateTitle,
                                    updateMealTag,
                                    updateIngr,
                                    updateInstr,
                                    updateDate,
                                    updateImage));
        UpdateOptions options = new UpdateOptions().upsert(true);
        recipeDocumentCollection.updateOne(filter, updates, options);
        return true;
    }

    @Override
    public Recipe find(String id) {
        Bson filter = eq("_id", new ObjectId(id));
        var recipeDocumentIter = recipeDocumentCollection.find(filter);
        Document recipeDocument = recipeDocumentIter.first();
        if (recipeDocument == null) {
            // Recipe does not exist
            return null;
        }
        return jsonToRecipe(recipeDocument);
    }

    @Override
    public boolean update(Recipe updatedRecipe) {
        Bson filter = eq("_id", updatedRecipe.getId());
        return true;
    }

    @Override
    public Recipe remove(String id) {
        Bson filter = eq("_id", new ObjectId(id));
        Document recipeDocument = recipeDocumentCollection.findOneAndDelete(filter);
        if (recipeDocument == null) {
            // Recipe does not exist
            return null;
        }
        return jsonToRecipe(recipeDocument);
    }

    @Override
    public void clear() {
        recipeDocumentCollection.drop();
    }

    @Override
    public int size() {
        return (int) recipeDocumentCollection.countDocuments();
    }

}
