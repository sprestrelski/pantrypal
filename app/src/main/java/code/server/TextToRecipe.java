package code.server;

import com.sun.net.httpserver.*;
import java.io.IOException;

public abstract class TextToRecipe {
    public abstract void handle(HttpExchange httpExchange) throws IOException;

    public String buildPrompt(String mealType, String ingredients) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("I am a student on a budget with a busy schedule and I need to quickly cook a ")
                .append(mealType)
                .append(". ")
                .append(ingredients)
                .append(" Make a recipe using only these ingredients plus condiments. ")
                .append("Remember to first include a title, then a list of ingredients, and then a list of instructions.");
        return prompt.toString();
    }

    public abstract void setSampleRecipe(String recipe);

}
