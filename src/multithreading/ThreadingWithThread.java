package multithreading;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.geometry.Insets;

class AppFrameThread extends FlowPane {
  private Button colorButton;
  private Button calcButton;
  private TextField colorField;
  private TextField calcField;
  Thread t;

  // Set a default style for buttons and fields - background color, font size,
  // italics
  String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
  String defaultFieldStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-alignment: CENTER;";

  AppFrameThread() {
    // Set properties for the flowpane
    this.setPrefSize(370, 120);
    this.setPadding(new Insets(5, 0, 5, 5));
    this.setVgap(10);
    this.setHgap(10);
    this.setPrefWrapLength(170);

    // Add the buttons and text fields
    colorButton = new Button("Color");
    colorButton.setStyle(defaultButtonStyle);

    colorField = new TextField();
    colorField.setStyle(defaultFieldStyle);

    calcButton = new Button("Calculate");
    calcButton.setStyle(defaultButtonStyle);

    calcField = new TextField();
    calcField.setStyle(defaultFieldStyle);

    this.getChildren().addAll(colorButton, colorField, calcButton, calcField);

    // Add the listeners to the buttons
    addListeners();
  }

  public void addListeners() {
    // Color Button
    colorButton.setOnAction(e -> {
      if (colorField.getBackground().getFills().get(0).getFill() == Color.GREEN) {
        colorField.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
      } else {
        colorField.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
      }
    });

    // Calculate Button
    calcButton.setOnAction(e -> {
      calculate();
    });
  }

  private void calculate() {
    Thread t = new Thread(
        new Runnable() {
          @Override
          public void run() {
            try {
              calcField.setText("Please Wait...");
              Thread.sleep(5 * 1000);
            } catch (InterruptedException e1) {
            }
            calcField.setText("Computation complete!");
          }
        });
    t.start();
  }
}

public class ThreadingWithThread extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {

    // Setting the Layout of the Window (Flow Pane)
    AppFrameThread root = new AppFrameThread();

    // Set the title of the app
    primaryStage.setTitle("Threading With Thread");
    // Create scene of mentioned size with the border pane
    primaryStage.setScene(new Scene(root, 370, 120));
    // Make window non-resizable
    primaryStage.setResizable(false);
    // Show the app
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
