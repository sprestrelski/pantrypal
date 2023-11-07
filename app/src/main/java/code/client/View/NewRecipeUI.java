package code.client.View;

import code.client.Controllers.*;
import code.client.Model.*;
import javafx.geometry.Pos;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.*;
// import javax.sound.sampled.*;

class MealTypeSelection extends VBox {
    private Label prompt;
    private Button recordButton;
    private ImageView microphone;
    private TextField mealTypeField;
    //=================FIRST PROMPT=================//
    MealTypeSelection() {
        // Get a picture of a microphone for the voice recording button
        File file = new File("app/src/main/java/code/client/View/microphone.png");
        microphone = new ImageView(new Image(file.toURI().toString()));
        // Set the size of the microphone image
        microphone.setFitWidth(30);
        microphone.setFitHeight(30);
        // Create a recording button
        recordButton = new Button();
        recordButton.setGraphic(microphone);
        // Set the user prompt for meal type selection
        prompt = new Label("Select Meal Type (Breakfast, Lunch, or Dinner)");
        prompt.setStyle("-fx-font-size: 16;");
        // Set a textField for the meal type that was selected
        mealTypeField = new TextField();
        mealTypeField.setPrefWidth(300);
        mealTypeField.setPromptText("Meal Type");
        // Add all of the elements to the MealTypeSelection
        this.getChildren().addAll(recordButton, prompt, mealTypeField);
    }
    public TextField getMealType() {
        return mealTypeField;
    }
    public Button getRecordButton() {
        return recordButton;
    }
}

class IngredientsList extends VBox {
    
    private Label prompt;
    private Button recordButton;
    private ImageView microphone;
    private TextField ingredientsField;
    //==============SECOND PROMPT=================//
    IngredientsList() {
        // Get a picture of a microphone for the voice recording button
        File file = new File("app/src/main/java/code/client/View/microphone.png");
        microphone = new ImageView(new Image(file.toURI().toString()));
        // Set the size of the microphone image
        microphone.setFitWidth(30);
        microphone.setFitHeight(30);
        // Create a recording button
        recordButton = new Button();
        recordButton.setGraphic(microphone);
        // Set the user prompt for meal type selection
        prompt = new Label("Please List Your Ingredients");
        prompt.setStyle("-fx-font-size: 16;");
        // Set a textField for the meal type that was selected
        ingredientsField = new TextField();
        ingredientsField.setPrefWidth(300);
        ingredientsField.setPromptText("Ingredients");
        // Add all of the elements to the MealTypeSelection
        this.getChildren().addAll(recordButton, prompt, ingredientsField);
    }
    public TextField getIngredients() {
        return ingredientsField;
    }
    public Button getRecordButton() {
        return recordButton;
    }
}

class Header extends HBox {
    Header() {
        this.setPrefSize(700, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Recipe Creation");
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER);
        
        
    }
}

class AppFrame extends BorderPane {
    // Inputs for the WhisperHandler
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN = "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";
    private static final String MODEL = "whisper-1";
    
    private boolean recording;
    private String mealType, ingredients;
    
    private Header header;
    private MealTypeSelection mealTypeSelection;
    private IngredientsList ingredientsList;
    private Button recordButton1, recordButton2;

    AppFrame() {
        header = new Header();
        mealTypeSelection = new MealTypeSelection();
        ingredientsList = new IngredientsList();
        
        this.setTop(header);
        this.setLeft(mealTypeSelection);
        this.setRight(ingredientsList);

        recordButton1 = mealTypeSelection.getRecordButton();
        recordButton2 = ingredientsList.getRecordButton();

        addListeners();
    }

    public void addListeners() {
        AudioRecorder recorder = new AudioRecorder(new Label("Recording..."));
        WhisperHandler audioProcessor = new WhisperHandler(API_ENDPOINT, TOKEN, MODEL);
        
        recordButton1.setOnAction(e -> {
            // recording = recorder.toggleRecording();

            if (!recording) {
                recorder.startRecording();
                recording = true;
                
            } else {
                recorder.stopRecording();
                recording = false;
                try {
                    mealType = audioProcessor.processAudio();
                } catch (IOException | URISyntaxException e2) {
                    e2.printStackTrace();
                } 
                mealTypeSelection.getMealType().setText(mealType);
            }
        });

        recordButton2.setOnAction(e -> {
            // recording = recorder.toggleRecording();
            if (!recording) {
                recorder.startRecording();
                recording = true;
                
            } else {
                recorder.stopRecording();
                recording = false;
                try {
                    ingredients = audioProcessor.processAudio();
                } catch (IOException | URISyntaxException e2) {
                    e2.printStackTrace();
                } 
                mealTypeSelection.getMealType().setText(ingredients);
            }
        });
    }
}

public class NewRecipeUI extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        AppFrame root = new AppFrame();
        Scene scene = new Scene(root, 700, 700);
        primaryStage.setTitle("Create New Recipe");
        primaryStage.setScene(scene);
        
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// package code.client.View;

// import javafx.geometry.Pos;
// import javafx.application.Application;
// import javafx.stage.Stage;
// import javafx.scene.Scene;
// import javafx.scene.control.*;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
// import javafx.scene.layout.*;
// import javafx.scene.text.*;

// public class NewRecipeUI extends Application {

//     @Override
//     public void start(Stage primaryStage) {
        
//         VBox root = new VBox();
//         root.setSpacing(20);
//         root.setAlignment(Pos.CENTER);
//         root.setStyle("-fx-background-color: #F0F8FF;");

        
//         Text title = new Text("New Recipe Setup");
//         title.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
//         root.getChildren().add(title);

   
//         VBox setupContainer = new VBox();
//         setupContainer.setSpacing(10);

//         //PROMPT 1
//         HBox option1Container = new HBox(10);
//         Label option1Label = new Label("1) Breakfast, Lunch, or Dinner?");
//         option1Label.setStyle("-fx-font-size: 16;"); // Increase the font size
//         Button microphoneButton1 = new Button();
//         Image microphoneImage = new Image(getClass().getResourceAsStream("microphone.png"));

//         ImageView microphoneImageView1 = new ImageView(microphoneImage);
//         microphoneImageView1.setFitHeight(30);
//         microphoneImageView1.setFitWidth(30);
//         microphoneButton1.setGraphic(microphoneImageView1);
//         option1Container.getChildren().addAll(option1Label, microphoneButton1);

//         // PROMPT 2
//         HBox option2Container = new HBox(10);
//         Label option2Label = new Label("2) List the ingredients you have");
//         option2Label.setStyle("-fx-font-size: 16;"); // Increase the font size
//         Button microphoneButton2 = new Button();
//         ImageView microphoneImageView2 = new ImageView(microphoneImage);
//         microphoneImageView2.setFitHeight(30);
//         microphoneImageView2.setFitWidth(30);
//         microphoneButton2.setGraphic(microphoneImageView2);
//         option2Container.getChildren().addAll(option2Label, microphoneButton2);

//         setupContainer.getChildren().addAll(option1Container, option2Container);

    
//         root.getChildren().add(setupContainer);

     
//         Button saveButton = new Button("Save Setup");
//         root.getChildren().add(saveButton);


//         Scene scene = new Scene(root, 700, 600);
//         primaryStage.setTitle("New Recipe Setup");
//         primaryStage.setScene(scene);
//         primaryStage.show();
//     }

//     public static void main(String[] args) {
//         launch(args);
//     }
// }
