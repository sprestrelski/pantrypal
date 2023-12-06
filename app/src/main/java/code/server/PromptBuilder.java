package code.server;

public class PromptBuilder {
    private final StringBuilder prompt;

    public PromptBuilder(String mealType, String ingredients) {
        prompt = new StringBuilder();
        prompt.append("I am a student on a budget with a busy schedule and I need to quickly cook a ")
                .append(mealType)
                .append(". ")
                .append(ingredients)
                .append(" Make a recipe using only these ingredients plus condiments. ")
                .append("Please give me a recipe in the following format with no comments after the instructions. Title: Ingredients: Instructions:");
    }

    public String getPrompt() {
        return prompt.toString();
    }
}
