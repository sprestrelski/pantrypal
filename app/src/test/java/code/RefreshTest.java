// package code;

// import code.client.Controllers.Controller;
// import code.client.Model.MockGPTService;
// import code.client.Model.MockDallEService;
// import code.client.Model.Model;
// import code.client.View.DetailsAppFrame;
// import code.client.View.View;
// import javafx.scene.control.Button;
// import javafx.stage.Stage;
// import org.junit.jupiter.api.Test;
// import code.client.Model.Account;
// import code.client.Model.AccountCSVReader;
// import code.client.Model.AccountCSVWriter;
// import code.client.Model.ChatGPTService;
// import code.client.View.*;
// import code.client.Controllers.*;
// import code.server.*;

// import java.io.IOException;
// import java.net.URISyntaxException;
// import java.util.*;

// public class RefreshTest {
// @Test
// public void refreshTesting() {
// DetailsAppFrame detailsAppFrame = new DetailsAppFrame();
// // TextToRecipe txt = new TextToRecipe();
// // initial recipe before refresh
// Recipe initialRecipe = detailsAppFrame.getDisplayedRecipe();

// MockGPTService mockGPT = new MockGPTService();
// Recipe refreshedRecipe;
// }

// private Recipe generateRefresh(Recipe originalRecipe, MockGPTService
// mockGPTService)
// throws IOException, InterruptedException, URISyntaxException {
// String mealType = "Breakfast";
// String ingredients = "Chicken, eggs.";

// String refreshedResponse = mockGPTService.getResponse(mealType, ingredients);
// Recipe out = mockGPTService.mapResponseToRecipe(mealType, refreshedResponse);

// return out;
// }

// }