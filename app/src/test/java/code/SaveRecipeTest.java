package code;

import org.junit.jupiter.api.Test;

import code.client.Controllers.AudioRecorder;
import code.client.Model.ITextToRecipe;
import code.client.Model.Recipe;
import code.client.Model.RecipeDb;
import code.client.Model.RecipeWriter;
import code.client.Model.TextToRecipe;
import code.client.View.DetailsAppFrame;
import code.client.View.RecipeDetailsUI;
import javafx.scene.control.Label;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.jupiter.api.BeforeEach;


/**
 * This file tests the Save recipe function to make sure it saves all recipes in database to the writer.
 */
public class SaveRecipeTest {
    
    @BeforeEach
    void setUp() {
        
    }

    @Test
    public void testSaveButtonEmpty() throws IOException {
        //RecipeDetailsUI mockedRecipe = new RecipeDetailsUI(new Recipe("1", "Spaghetti Bolognese"));
        Writer tempWriter = new StringWriter();
        RecipeWriter writer = new RecipeWriter(tempWriter);
        RecipeDb recipeDb = new RecipeDb();
        writer.writeRecipeDb(recipeDb);

        // NAME OF RECIPE | INGREDIENTS | INSTRUCTIONS 
        String expected = "sep=|\nRecipe Name| Ingredients| Instructions\n";
        assertEquals(expected, tempWriter.toString());
        
    }

    @Test
    public void testSaveButtonNotEmpty() throws IOException {
        Recipe mockedRecipe = new Recipe("1", "Spaghetti Bolognese");
        Writer tempWriter = new StringWriter();
        RecipeWriter writer = new RecipeWriter(tempWriter);
        RecipeDb recipeDb = new RecipeDb();
        recipeDb.add(mockedRecipe);
        writer.writeRecipeDb(recipeDb);

        // NAME OF RECIPE | INGREDIENTS | INSTRUCTIONS 
        String expected = "sep=|\nRecipe Name| Ingredients| Instructions\nSpaghetti Bolognese| | \n";// will set it later
        assertEquals(expected, tempWriter.toString());

        /*Recipe savedRecipeVar = recipeDb.iterator().next();
        assertEquals(mockedRecipe.getRecipe(), savedRecipeVar);*/
        
    }
}

