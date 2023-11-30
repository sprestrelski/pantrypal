package code;

import org.junit.jupiter.api.Test;

import code.client.Model.TextToRecipe;
import code.client.Model.Recipe;
import code.client.Model.MockGPTService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextToRecipeTest {

    @Test
    /**
     * Integration test for provide recipe
     */
    public void testPromptBuild() {
        TextToRecipe textToRecipe = new MockGPTService();
        String prompt = "I am a student on a budget with a busy schedule and I need to quickly cook a Lunch. I have rice, shrimp, chicken, and eggs. Make a recipe using only these ingredients plus condiments. Remember to first include a title, then a list of ingredients, and then a list of instructions.";
        String response = textToRecipe.buildPrompt("Lunch", "I have rice, shrimp, chicken, and eggs.");
        assertEquals(prompt, response);
    }

    @Test
    /**
     * Unit tests for Recipe features
     */
    public void testParseJSON() {
        TextToRecipe textToRecipe = new MockGPTService();
        String mealType = "BREAKFAST";
        String responseText = """
                Fried Chicken and Egg Fried Rice

                Ingredients:

                - 2 chicken breasts, diced
                - 2 large eggs
                - 2 cups cooked rice
                - 2 tablespoons vegetable oil
                - 2 tablespoons soy sauce
                - 1 teaspoon sesame oil
                - Salt and pepper to taste

                Instructions:

                1. Heat the vegetable oil in a large pan over medium-high heat.

                2. Add the diced chicken and cook until the chicken is cooked through, about 8 minutes.

                3. In a separate bowl, beat the eggs and season with salt and pepper.

                4. Reduce the heat to medium and add the eggs to the pan with the chicken.

                5. Using a spatula, scramble the eggs until they are cooked through.

                6. Add in the cooked rice and soy sauce and stir to combine with the chicken and eggs.

                7. Cook the fried rice until everything is heated through, about 5 minutes.

                8. Drizzle with sesame oil, season with more salt and pepper if desired, and serve. Enjoy!
                """;
        String parsedResponse = """
                Title: Fried Chicken and Egg Fried Rice
                Ingredients:
                2 chicken breasts, diced
                2 large eggs
                2 cups cooked rice
                2 tablespoons vegetable oil
                2 tablespoons soy sauce
                1 teaspoon sesame oil
                Salt and pepper to taste
                Instructions:
                Heat the vegetable oil in a large pan over medium-high heat.
                Add the diced chicken and cook until the chicken is cooked through, about 8 minutes.
                In a separate bowl, beat the eggs and season with salt and pepper.
                Reduce the heat to medium and add the eggs to the pan with the chicken.
                Using a spatula, scramble the eggs until they are cooked through.
                Add in the cooked rice and soy sauce and stir to combine with the chicken and eggs.
                Cook the fried rice until everything is heated through, about 5 minutes.
                Drizzle with sesame oil, season with more salt and pepper if desired, and serve. Enjoy!
                """;
        Recipe recipe = textToRecipe.mapResponseToRecipe(mealType, responseText);
        assertEquals(parsedResponse, recipe.toString());
    }

    @Test
    public void testParseNoIndentsAndNewLines() {
        TextToRecipe textToRecipe = new MockGPTService();
        String mealType = "LUNCH";
        String responseText = """
                Cheesy pasta bake
                Ingredients:
                - 1 lb pasta
                - 1 lb ground beef
                - 1 Tbsp olive oil
                - 1 onion, diced
                - 3 garlic cloves, minced
                - 1 1/2 cans diced tomatoes
                - 1 tsp oregano
                - 1 tsp basil
                - 1/2 tsp red pepper flakes
                - Salt and pepper to taste
                - 2-3 cups of shredded cheese
                - Garlic bread
                Instructions:
                1. Preheat oven to 375 degrees F.
                2. Cook pasta according to package instructions. Drain and set aside.
                3. Meanwhile, heat the olive oil in a large skillet over medium-high heat.
                4. Add the onion and garlic and cook until lightly browned and fragrant, about 3 minutes.
                5. Add the ground beef and cook until browned and crumbled, about 5-8 minutes.
                6. Add the tomatoes, oregano, basil, and red pepper flakes and season with salt and pepper to taste. Simmer the mixture for 8-10 minutes, or until the flavors have developed.
                7. In a greased 9x13 inch baking dish, combine the cooked pasta and beef mixture.
                8. Top with shredded cheese and bake in preheated oven for 25-30 minutes, or until the cheese is melted and bubbly.
                9. Serve the cheesy pasta bake with garlic bread. Enjoy!
                """;
        String parsedResponse = """
                Title: Cheesy pasta bake
                Ingredients:
                1 lb pasta
                1 lb ground beef
                1 Tbsp olive oil
                1 onion, diced
                3 garlic cloves, minced
                1 1/2 cans diced tomatoes
                1 tsp oregano
                1 tsp basil
                1/2 tsp red pepper flakes
                Salt and pepper to taste
                2-3 cups of shredded cheese
                Garlic bread
                Instructions:
                Preheat oven to 375 degrees F.
                Cook pasta according to package instructions. Drain and set aside.
                Meanwhile, heat the olive oil in a large skillet over medium-high heat.
                Add the onion and garlic and cook until lightly browned and fragrant, about 3 minutes.
                Add the ground beef and cook until browned and crumbled, about 5-8 minutes.
                Add the tomatoes, oregano, basil, and red pepper flakes and season with salt and pepper to taste. Simmer the mixture for 8-10 minutes, or until the flavors have developed.
                In a greased 9x13 inch baking dish, combine the cooked pasta and beef mixture.
                Top with shredded cheese and bake in preheated oven for 25-30 minutes, or until the cheese is melted and bubbly.
                Serve the cheesy pasta bake with garlic bread. Enjoy!
                """;
        Recipe recipe = textToRecipe.mapResponseToRecipe(mealType, responseText);
        assertEquals(parsedResponse, recipe.toString());
    }

    @Test
    public void testParseDifferentLineSpacing() {
        TextToRecipe textToRecipe = new MockGPTService();
        String mealType = "DINNER";
        String responseText = """


                Savory Beef Pasta Bake
                Ingredients:
                - ½ pound of ground beef
                - 1 box of your favorite pasta noodles
                - 1 onion, chopped
                - 4 cloves of garlic, minced
                - 1 jar of your favorite marinara sauce
                - 2 cups of shredded cheese (Mozzarella or Italian blend)
                - 1 loaf of garlic bread
                - Olive oil
                - Salt and pepper to taste

                Instructions:

                1. Preheat oven to 350°F.

                2. Bring a large pot of salted water to a boil over high heat.

                3. In a large skillet, heat the olive oil over medium-high heat. Add the chopped onion and minced garlic and sauté until the onion is soft and fragrant, about 3 minutes.

                4. Add the ground beef and cook until the beef is no longer pink, about 5 minutes, stirring frequently.

                5. Add the marinara sauce and stir to combine. Simmer for 10 minutes.

                6. Meanwhile, add the pasta to the pot of boiling water and cook for 6 minutes or according to the package directions.

                7. Drain the cooked pasta and add it to the skillet with the beef and sauce. Stir to combine.

                8. Transfer the pasta and beef mixture to a 9?x13? baking dish. Sprinkle the top with the shredded cheese.

                9. Place the baking dish in the preheated oven and bake for 15 minutes.

                10. Remove from oven and discard the garlic bread or serve alongside the beef pasta bake.

                11. Enjoy!
                    """;
        String parsedResponse = """
                Title: Savory Beef Pasta Bake
                Ingredients:
                ½ pound of ground beef
                1 box of your favorite pasta noodles
                1 onion, chopped
                4 cloves of garlic, minced
                1 jar of your favorite marinara sauce
                2 cups of shredded cheese (Mozzarella or Italian blend)
                1 loaf of garlic bread
                Olive oil
                Salt and pepper to taste
                Instructions:
                Preheat oven to 350°F.
                Bring a large pot of salted water to a boil over high heat.
                In a large skillet, heat the olive oil over medium-high heat. Add the chopped onion and minced garlic and sauté until the onion is soft and fragrant, about 3 minutes.
                Add the ground beef and cook until the beef is no longer pink, about 5 minutes, stirring frequently.
                Add the marinara sauce and stir to combine. Simmer for 10 minutes.
                Meanwhile, add the pasta to the pot of boiling water and cook for 6 minutes or according to the package directions.
                Drain the cooked pasta and add it to the skillet with the beef and sauce. Stir to combine.
                Transfer the pasta and beef mixture to a 9?x13? baking dish. Sprinkle the top with the shredded cheese.
                Place the baking dish in the preheated oven and bake for 15 minutes.
                Remove from oven and discard the garlic bread or serve alongside the beef pasta bake.
                Enjoy!
                """;
        Recipe recipe = textToRecipe.mapResponseToRecipe(mealType, responseText);
        assertEquals(parsedResponse, recipe.toString());
    }

    @Test
    public void testParseRemoveDashesAndNumbers() {
        TextToRecipe textToRecipe = new MockGPTService();
        String mealType = "LUNCH";
        String responseText = """


                Savory Beef Pasta Bake
                Ingredients:
                - ½ pound of ground beef
                - 1 box of your favorite pasta noodles
                - 1 onion, chopped
                - 4 cloves of garlic, minced
                - 1 jar of your favorite marinara sauce
                - 2 cups of shredded cheese (Mozzarella or Italian blend)
                - 1 loaf of garlic bread
                - Olive oil
                - Salt and pepper to taste

                Instructions:

                1. Preheat oven to 350°F.

                2. Bring a large pot of salted water to a boil over high heat.

                3. In a large skillet, heat the olive oil over medium-high heat. Add the chopped onion and minced garlic and sauté until the onion is soft and fragrant, about 3 minutes.

                4. Add the ground beef and cook until the beef is no longer pink, about 5 minutes, stirring frequently.

                5. Add the marinara sauce and stir to combine. Simmer for 10 minutes.

                6. Meanwhile, add the pasta to the pot of boiling water and cook for 6 minutes or according to the package directions.

                7. Drain the cooked pasta and add it to the skillet with the beef and sauce. Stir to combine.

                8. Transfer the pasta and beef mixture to a 9?x13? baking dish. Sprinkle the top with the shredded cheese.

                9. Place the baking dish in the preheated oven and bake for 15 minutes.

                10. Remove from oven and discard the garlic bread or serve alongside the beef pasta bake.

                11. Enjoy!
                    """;
        String parsedResponse = """
                Title: Savory Beef Pasta Bake
                Ingredients:
                ½ pound of ground beef
                1 box of your favorite pasta noodles
                1 onion, chopped
                4 cloves of garlic, minced
                1 jar of your favorite marinara sauce
                2 cups of shredded cheese (Mozzarella or Italian blend)
                1 loaf of garlic bread
                Olive oil
                Salt and pepper to taste
                Instructions:
                Preheat oven to 350°F.
                Bring a large pot of salted water to a boil over high heat.
                In a large skillet, heat the olive oil over medium-high heat. Add the chopped onion and minced garlic and sauté until the onion is soft and fragrant, about 3 minutes.
                Add the ground beef and cook until the beef is no longer pink, about 5 minutes, stirring frequently.
                Add the marinara sauce and stir to combine. Simmer for 10 minutes.
                Meanwhile, add the pasta to the pot of boiling water and cook for 6 minutes or according to the package directions.
                Drain the cooked pasta and add it to the skillet with the beef and sauce. Stir to combine.
                Transfer the pasta and beef mixture to a 9?x13? baking dish. Sprinkle the top with the shredded cheese.
                Place the baking dish in the preheated oven and bake for 15 minutes.
                Remove from oven and discard the garlic bread or serve alongside the beef pasta bake.
                Enjoy!
                """;
        Recipe recipe = textToRecipe.mapResponseToRecipe(mealType, responseText);
        assertEquals(parsedResponse, recipe.toString());
    }
}
