
// package code.client.View;

// import javafx.scene.layout.StackPane;

// import javafx.application.Application;
// import javafx.stage.Stage;
// import javafx.scene.Scene;
// import javafx.scene.control.*;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
// import javafx.stage.FileChooser;
// import javafx.stage.FileChooser.ExtensionFilter;
// import javafx.scene.layout.*;
// import javafx.scene.text.*;
// import java.io.*;
// import javafx.geometry.Pos;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.Comparator;

// class ImageUploader {
//     public static void uploadImage(Stage primaryStage, ImageView imageView) {
//         FileChooser fileChooser = new FileChooser();
//         fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
//         File selectedFile = fileChooser.showOpenDialog(primaryStage);
//         if (selectedFile != null) {
//             Image image = new Image(selectedFile.toURI().toString());
//             imageView.setImage(image);
//         }
//     }
// }

// class Recipe extends HBox {
//     private ImageView recipeImage;
//     private Label index, nameLabel;
//     private GridPane recipeDetailsGrid;
//     private TextField nameField;
//     private Button selectButton, uploadButton;
//     private boolean selected;

//     Recipe() {
//         selected = false;

//         index = new Label();
//         index.setText("");
//         index.setPrefSize(30, 120);
//         index.setAlignment(Pos.CENTER);
//         index.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
//         this.getChildren().add(index);

//         recipeDetailsGrid = new GridPane();
//         recipeDetailsGrid.setPrefSize(350, 120);
//         recipeDetailsGrid.setAlignment(Pos.CENTER);
//         recipeDetailsGrid.setVgap(10);
//         recipeDetailsGrid.setHgap(10);
//         recipeDetailsGrid.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

//         uploadButton = new Button("Upload Image");
//         uploadButton.setPrefSize(100, 100);
//         uploadButton.setPrefHeight(Double.MAX_VALUE);
//         uploadButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
//         this.getChildren().add(uploadButton);

//         recipeImage = new ImageView();
//         recipeImage.setFitWidth(120);
//         recipeImage.setFitHeight(120);
//         File file = new File("src/default_recipe.jpg");
//         Image image = new Image(file.toURI().toString());
//         recipeImage.setImage(image);
//         this.getChildren().add(recipeImage);

//         nameLabel = new Label("Recipe Name:");
//         nameField = new TextField();
//         nameField.setPromptText("Name");
//         recipeDetailsGrid.add(nameLabel, 0, 0);
//         recipeDetailsGrid.add(nameField, 1, 0);

//         this.getChildren().add(recipeDetailsGrid);

//         selectButton = new Button("Select");
//         selectButton.setPrefSize(100, 120);
//         selectButton.setPrefHeight(Double.MAX_VALUE);
//         selectButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
//         this.getChildren().add(selectButton);
//     }

//     public void setRecipeIndex(int num) {
//         this.index.setText(num + "");
//     }

//     public Button getUploadButton() {
//         return this.uploadButton;
//     }

//     public ImageView getRecipeImage() {
//         return this.recipeImage;
//     }

//     public TextField getRecipeName() {
//         return this.nameField;
//     }

//     public Button getSelectButton() {
//         return this.selectButton;
//     }

//     public boolean isSelected() {
//         return this.selected;
//     }

//     public void toggleSelected() {
//         if (selected == false) {
//             selected = true;
//             for (int i = 0; i < this.getChildren().size(); i++) {
//                 this.getChildren().get(i).setStyle("-fx-background-color: #9EBCE2; -fx-border-width: 0;");
//             }
//         } else {
//             selected = false;
//             for (int i = 0; i < this.getChildren().size(); i++) {
//                 this.getChildren().get(i).setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
//             }
//         }
//     }
// }


// class RecipeList extends VBox {

//     RecipeList() {
//         this.setSpacing(5);
//         this.setPrefSize(700, 600);
//         this.setStyle("-fx-background-color: #F0F8FF;");
//     }

//     public void updateRecipeIndices() {
//         int index = 1;
//         for (int i = 0; i < this.getChildren().size(); i++) {
//             if (this.getChildren().get(i) instanceof Recipe) {
//                 ((Recipe) this.getChildren().get(i)).setRecipeIndex(index);
//                 index++;
//             }
//         }
//     }

//     public void removeSelectedRecipes() {
//         this.getChildren().removeIf(recipe -> recipe instanceof Recipe && ((Recipe) recipe).isSelected());
//         this.updateRecipeIndices();
//     }

//     public void loadRecipes() {
//         try {
//             BufferedReader reader = new BufferedReader(new FileReader("recipes.csv"));
//             String line;
//             String[] recipeInfo;
//             reader.readLine();
//             while ((line = reader.readLine()) != null) {
//                 recipeInfo = line.split(", ");
//                 Recipe recipe = new Recipe();
//                 recipe.getRecipeImage().setImage(new Image(new File("src/default_recipe.jpg").toURI().toString()));
//                 recipe.getRecipeName().setText(recipeInfo[0]);
//                 recipe.getUploadButton().setOnAction(e1 -> {
//                     ImageUploader.uploadImage(new Stage(), recipe.getRecipeImage());
//                 });
//                 recipe.getSelectButton().setOnAction(e2 -> {
//                     recipe.toggleSelected();
//                 });
//                 this.getChildren().add(recipe);
//             }
//             reader.close();
//         } 
//         catch (IOException e) {
//             System.out.println("Recipes could not be loaded.");
//         }
//         this.updateRecipeIndices();
//     }

//     public void saveRecipes() {
//         try {
//             BufferedWriter writer = new BufferedWriter(new FileWriter("recipes.csv"));
//             writer.write("Name, Ingredients, Instructions");
//             writer.newLine();
//             for (int i = 0; i < this.getChildren().size(); i++) {
//                 writer.write(((Recipe) this.getChildren().get(i)).getRecipeName().getText() + ", ");
//                 writer.newLine();
//             }
//             writer.close();
//         }
//         catch (IOException e) {
//             System.out.println("Recipes could not be saved.");
//         }
//     }

//     public void sortRecipes() {
//         ArrayList<Recipe> recipes = new ArrayList<Recipe>();
//         Recipe copy;
//         for (int i = 0; i < this.getChildren().size(); i++) {
//             copy = new Recipe();
//             copy.getRecipeName().setText(((Recipe) this.getChildren().get(i)).getRecipeName().getText());
//             copy.getRecipeImage().setImage(((Recipe) this.getChildren().get(i)).getRecipeImage().getImage());
//             recipes.add(copy);
//         }

//         Collections.sort(recipes, new Comparator<Recipe>() {
//             @Override
//             public int compare(Recipe r1, Recipe r2) {
//                 return r1.getRecipeName().getText().compareTo(r2.getRecipeName().getText());
//             }
//         });

//         for (int i = 0; i < this.getChildren().size(); i++) {
//             ((Recipe) this.getChildren().get(i)).getRecipeName().setText(recipes.get(i).getRecipeName().getText());
//             ((Recipe) this.getChildren().get(i)).getRecipeImage().setImage(recipes.get(i).getRecipeImage().getImage());
//         }
//     }
// }

// class Footer extends HBox {
//     private Button addButton;
//     private Button deleteButton;
//     private Button loadButton;
//     private Button saveButton;
//     private Button sortButton;

//     Footer() {
//         this.setPrefSize(700, 60);
//         this.setStyle("-fx-background-color: #F0F8FF;");
//         this.setSpacing(15);

//         String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 11 arial;";

//         addButton = new Button("New Recipe");
//         addButton.setStyle(defaultButtonStyle);
//         deleteButton = new Button("Delete Selected");
//         deleteButton.setStyle(defaultButtonStyle);
//         loadButton = new Button("Load Recipes");
//         loadButton.setStyle(defaultButtonStyle);
//         saveButton = new Button("Save Recipes");
//         saveButton.setStyle(defaultButtonStyle);
//         sortButton = new Button("Sort Recipes (By Name)");
//         sortButton.setStyle(defaultButtonStyle);

//         this.getChildren().addAll(addButton, deleteButton, loadButton, saveButton, sortButton);
//         this.setAlignment(Pos.CENTER);
//     }

//     public Button getAddButton() {
//         return addButton;
//     }

//     public Button getDeleteButton() {
//         return deleteButton;
//     }

//     public Button getLoadButton() {
//         return loadButton;
//     }

//     public Button getSaveButton() {
//         return saveButton;
//     }

//     public Button getSortButton() {
//         return sortButton;
//     }
// }

// class Header extends HBox {

//     Header() {
//         this.setPrefSize(700, 60);
//         this.setStyle("-fx-background-color: #F0F8FF;");

//         Text titleText = new Text("Recipe List");
//         titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
//         this.getChildren().add(titleText);
//         this.setAlignment(Pos.CENTER);
//     }
// }

// class AppFrame extends BorderPane {
//     private Header header;
//     private Footer footer;
//     private RecipeList recipeList;
//     private Button addButton, deleteButton, loadButton, saveButton, selectButton, sortButton, uploadButton;

//     AppFrame() {
//         header = new Header();
//         recipeList = new RecipeList();
//         footer = new Footer();

//         ScrollPane scroller = new ScrollPane(recipeList);
//         scroller.setFitToWidth(true);
//         scroller.setFitToHeight(true);

//         this.setTop(header);
//         this.setCenter(scroller);
//         this.setBottom(footer);

//         addButton = footer.getAddButton();
//         deleteButton = footer.getDeleteButton();
//         loadButton = footer.getLoadButton();
//         saveButton = footer.getSaveButton();
//         sortButton = footer.getSortButton();

//         addListeners();
//     }

//     public void addListeners() {
//         addButton.setOnAction(e -> {
//             Recipe recipe = new Recipe();
//             recipeList.getChildren().add(recipe);

//             uploadButton = recipe.getUploadButton();
//             uploadButton.setOnAction(e1 -> {
//                 ImageUploader.uploadImage(new Stage(), recipe.getRecipeImage());
//             });

//             selectButton = recipe.getSelectButton();
//             selectButton.setOnAction(e2 -> {
//                 recipe.toggleSelected();
//             });

//             recipeList.updateRecipeIndices();
//         });

//         deleteButton.setOnAction(e -> {
//             recipeList.removeSelectedRecipes();
//         });

//         loadButton.setOnAction(e -> {
//             recipeList.loadRecipes();
//         });

//         saveButton.setOnAction(e -> {
//             recipeList.saveRecipes();
//         });

//         sortButton.setOnAction(e -> {
//             recipeList.sortRecipes();
//         });
//     }
// }

// public class Main extends Application {

//     @Override
//     public void start(Stage primaryStage) throws Exception {
//         AppFrame root = new AppFrame();

//         primaryStage.setTitle("Recipe Management App");
//         primaryStage.setScene(new Scene(root, 700, 600));
//         primaryStage.setResizable(false);
//         primaryStage.show();
//     }

//     public static void main(String[] args) {
//         launch(args);
//     }
// }
