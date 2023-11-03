
// layout
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;

//images
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

// data structures
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

class Contact extends HBox {

    // private Label index;
    private boolean markedDel;

    // contact image
    private ImageView contactImage;

    // info fields
    private TextField contactName;
    private TextField phoneNum;
    private TextField emailAddr;

    private Button delButton;
    private GridPane pane;

    Contact() {
        // set size and background color of contact
        this.setPrefSize(600, 50);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
        Font biggerFont = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 22);
        Font smallerFont = Font.font("georgia", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 14);

        ///////////////////////////
        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(5);
        pane.setVgap(10);
        pane.setPadding(new Insets(2.5, 5, 5, 5)); // set top, right, bottom, left
        // this allows input fields to be placed in the window

        //////////////////////////
        try {

            // TODO: Make profile pictures align nicely
            Image image = new Image(
                    new FileInputStream("images/profile.png"));
            contactImage = new ImageView(image);
            contactImage.setFitWidth(50);
            contactImage.setFitHeight(50);

            // Add a click event handler for the contact image
            contactImage.setOnMouseClicked(event -> {
                // Implement your code to handle image upload here
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters()
                        .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
                File selectedFile = fileChooser.showOpenDialog(getScene().getWindow());

                if (selectedFile != null) {
                    // Load the selected image and update the contactImage ImageView
                    Image newImage = new Image(selectedFile.toURI().toString());
                    contactImage.setImage(newImage);
                }
            });
        } catch (FileNotFoundException e) {

        }

        contactName = new TextField(); // create name text field
        contactName.setPrefSize(250, 8); // set size of text field
        contactName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        // how much away from the (top, left, bottom, right)
        contactName.setAlignment(Pos.CENTER);
        contactName.setPromptText("Name");
        contactName.setFont(biggerFont);

        phoneNum = new TextField();
        phoneNum.setPrefSize(250, 8);
        phoneNum.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        phoneNum.setAlignment(Pos.CENTER);
        phoneNum.setPromptText("Phone Number");
        phoneNum.setFont(biggerFont);

        emailAddr = new TextField();
        emailAddr.setPrefSize(250, 30); // set size of text field
        emailAddr.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        emailAddr.setFont(smallerFont);
        emailAddr.setPromptText("Email Address");
        emailAddr.setAlignment(Pos.CENTER);

        delButton = new Button("Select");
        delButton.setPrefSize(100, 20);
        delButton.setPrefHeight(Double.MAX_VALUE);
        delButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // sets style of button
        delButton.setOnAction(e -> {
            toggleDel();
        });

        setAlignment(Pos.CENTER_LEFT);
        setSpacing(30.0);
        HBox.setMargin(contactName, new Insets(0, 0, 0, 50));

        contactName.setPrefColumnCount(14);
        phoneNum.setPrefColumnCount(14);
        pane.add(contactImage, 0, 0);
        pane.add(contactName, 1, 0);
        pane.add(phoneNum, 6, 0);
        pane.add(delButton, 10, 0);
        pane.add(emailAddr, 1, 1);
        this.getChildren().add(pane);
    }

    // Getter methods
    public TextField getContactName() {
        return this.contactName;
    }

    public TextField getPhoneNumber() {
        return this.phoneNum;
    }

    public TextField getEmailAddress() {
        return this.emailAddr;
    }

    public Button getDelButton() {
        return this.delButton;
    }

    public boolean isMarkedDel() {
        return this.markedDel;
    }

    /**
     * This function toggles the state of whether or not a certain contact has been
     * selected (to be deleted)
     * 
     * @return boolean representing if any contacts have been selected
     */
    public boolean toggleDel() {
        markedDel = !markedDel;
        if (markedDel) {
            for (int i = 0; i < this.getChildren().size(); i++) {
                // change contact color to red
                this.getChildren().get(i).setStyle("-fx-background-color: #F07470; -fx-border-width: 0;");
            }
        } else {
            // change contact color to green
            for (int i = 0; i < this.getChildren().size(); i++) {
                this.getChildren().get(i).setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
            }
        }
        return markedDel;
    }
}

class ContactList extends VBox {

    ContactList() {
        this.setSpacing(5); // sets spacing between contacts
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void deleteMarked() {
        this.getChildren().removeIf(contact -> contact instanceof Contact && ((Contact) contact).isMarkedDel());
    }

    /*
     * Load contacts from a file called "contacts.txt"
     * Add the contacts to the children of contactList component
     */
    public void loadContacts() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("contacts.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.contains("contactName, ")) {
                    continue;
                }
                Contact contact = new Contact();
                String[] fields = line.split(", ");
                contact.getContactName().setText(fields[0]);
                contact.getPhoneNumber().setText(fields[1]);
                contact.getEmailAddress().setText(fields[2]);
                this.getChildren().add(contact);

            }
            br.close();
        } catch (Exception e) {
            System.out.println("Could not load contacts from file \"contacts.txt\"");
        }
    }

    /*
     * Save contacts to a file called "contacts.txt"
     */
    public void saveContacts() {
        try {
            FileWriter fw = new FileWriter("contacts.txt");
            fw.write("contactName, phoneNum, emailAddr\n");
            for (int i = 0; i < this.getChildren().size(); i++) {
                if (this.getChildren().get(i) instanceof Contact) {
                    Contact contact = ((Contact) this.getChildren().get(i));
                    String toWrite = contact.getContactName().getText() + ", " + contact.getPhoneNumber().getText()
                            + ", " + contact.getEmailAddress().getText() + "\n";
                    fw.write(toWrite);
                }
            }
            fw.close();
        } catch (Exception e) {
            System.out.println("Could not write to \"contacts.txt\"");
        }
    }

    //
    /*
     * Sort the contacts lexicographically by name
     */
    public void sortContacts() {
        // Setting up for sorting
        ArrayList<String> namesL = new ArrayList<>();
        ArrayList<Contact> contacts = new ArrayList<>(getContactList()); // Getting a deep copy.

        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof Contact) {
                String temp = ((Contact) this.getChildren().get(i)).getContactName().getText();

                // Stores string name and its index (which won't mess with the sorting)
                namesL.add(temp.toUpperCase() + i);
            }
        }
        Collections.sort(namesL);
        this.getChildren().clear();
        for (int i = 0; i < namesL.size(); i++) {
            String curr = namesL.get(i);
            int idxInContacts = Integer.parseInt(curr.substring(curr.length() - 1));
            this.getChildren().add(contacts.get(idxInContacts));
        }

    }

    /**
     * This method functions as a helper method for sorting, by gathering all
     * contacts.
     * 
     * @return the whole list of contacts as an ArrayList
     */
    public ArrayList<Contact> getContactList() {

        ArrayList<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof Contact) {
                contacts.add(((Contact) this.getChildren().get(i)));
            }
        }
        return contacts;
    }
}

class Footer extends HBox {
    private Button loadButton;
    private Button saveButton;
    private Button confirmDelete;

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        loadButton = new Button("Load Contacts");
        loadButton.setStyle(defaultButtonStyle);

        saveButton = new Button("Save Contacts");
        saveButton.setStyle(defaultButtonStyle);

        confirmDelete = new Button("Delete Selected");
        confirmDelete.setStyle(defaultButtonStyle); // sets style of button

        this.setSpacing(80.0);
        this.getChildren().addAll(loadButton, saveButton, confirmDelete); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getLoadButton() {
        return loadButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getDelAllButton() {
        return confirmDelete;
    }
}

class Header extends HBox {
    private Button addButton;
    private Button sortButton;

    Header() {
        this.setPrefSize(600, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        // text of the header, spaces included for style
        Text titleText = new Text("Contact Manager                                                               ");
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        addButton = new Button("Add New Contact"); // text displayed on add button
        addButton.setStyle(defaultButtonStyle); // styling the button

        sortButton = new Button("Sort All Contacts");
        sortButton.setStyle(defaultButtonStyle);

        this.setSpacing(15.0);
        this.getChildren().addAll(addButton, sortButton);
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getSortButton() {
        return sortButton;
    }
}

class AppFrame extends BorderPane {

    private Header header;
    private Footer footer;
    private ContactList contactList;

    private Button addButton;
    private Button loadButton;
    private Button saveButton;
    private Button sortButton;
    private Button delAllButton;
    String defaultButtonStyle;

    AppFrame() {
        // Initialize the header Object
        header = new Header();

        // Create a contactList Object to hold the contacts
        contactList = new ContactList();

        // Initialize the Footer Object
        footer = new Footer();

        ScrollPane scroller = new ScrollPane(contactList);
        scroller.setFitToHeight(true);
        scroller.setFitToWidth(true);

        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scroller);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);

        // Initialize Button Variables through the getters in Footer and Header
        addButton = header.getAddButton();
        loadButton = footer.getLoadButton();
        saveButton = footer.getSaveButton();
        sortButton = header.getSortButton();
        delAllButton = footer.getDelAllButton();
        // Call Event Listeners for the Buttons
        addListeners();
        defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
    }

    public void addListeners() {

        // Add button functionality
        addButton.setOnAction(e -> {
            // Create a new contact and add to contactList
            addButton.setStyle("-fx-background-color: #808080; -fx-border-width: 0;");
            Contact contact = new Contact();
            contactList.getChildren().add(contact);
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(f -> addButton.setStyle(defaultButtonStyle));
            pause.play();
            // ability to toggle deletion
            Button delButton = contact.getDelButton();
            delButton.setOnAction(e1 -> {

                // Call toggleDel on click
                boolean atLeastOneSelected = contact.toggleDel();

                // Checks to see if any contacts are selected and to make delete selected button
                // red
                if (atLeastOneSelected) {
                    delAllButton.setStyle("-fx-background-color: #DC1C13; -fx-border-width: 0;");
                } else {
                    delAllButton.setStyle(defaultButtonStyle);
                }
            });

        });

        loadButton.setOnAction(e -> {
            loadButton.setStyle("-fx-background-color: #90EE90; -fx-border-width: 0;"); // #90EE90
            contactList.loadContacts();
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(f -> loadButton.setStyle(defaultButtonStyle));
            pause.play();
        });

        saveButton.setOnAction(e -> {
            saveButton.setStyle("-fx-background-color: #90EE90; -fx-border-width: 0;"); // #90EE90
            contactList.saveContacts();
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(f -> saveButton.setStyle(defaultButtonStyle));
            pause.play();

        });

        sortButton.setOnAction(e -> {
            sortButton.setStyle("-fx-background-color: #0D98BA; -fx-border-width: 0;");
            contactList.sortContacts();
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(f -> sortButton.setStyle(defaultButtonStyle));
            pause.play();
        });

        delAllButton.setOnAction(e -> {
            contactList.deleteMarked();
            delAllButton.setStyle(defaultButtonStyle);
        });
    }
}

public class ContactManagerReference extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, Footer and the
        // Main Window
        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("Contact Manager");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 800, 600));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
