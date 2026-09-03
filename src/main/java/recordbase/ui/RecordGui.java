package recordbase.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
// import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import recordbase.Record;

/**
 * JavaFX GUI for the Record application.
 *
 * <p>The interface follows a chatbot-style layout, with user input
 * appearing in the upper-right section and a text input area at
 * the bottom of the window.</p>
 */
public class RecordGui extends Application {
    private TextArea userInputArea;
    private VBox userMessageContainer;

    private VBox dialogContainer;
    private TextField userInput;

    // These images must be located within "src/main/resources/images"
    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/SmallLogo.png"));
    private Image recordImage = new Image(this.getClass().getResourceAsStream("/images/SmallLogo.png"));

    private Record record = new Record();

    /**
     * Starts the JavaFX application.
     *
     * @param stage the primary stage
     */
    @Override
    public void start(Stage stage) {
        // BorderPane root = createRoot();

        // Scene scene = new Scene(root, 700, 600);

        // stage.setTitle("Record");
        // stage.setScene(scene);
        // stage.show();

        // -----Part 1a-----
        // Label helloWorld = new Label("Hello World");
        // Scene scene = new Scene(helloWorld);

        // stage.setScene(scene);
        // stage.show();
        // -----End Part 1a-----

        // -----Part 2a-----
        // scrollPane = new ScrollPane();
        // dialogContainer = new VBox();
        // scrollPane.setContent(dialogContainer);

        // userInput = new TextField();
        // sendButton = new Button("Send");

        // // DialogBox dialogBox = new DialogBox("Hello!", userImage);
        // // dialogContainer.getChildren().addAll(dialogBox);

        // AnchorPane mainLayout = new AnchorPane();
        // mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        // scene = new Scene(mainLayout);

        // stage.setScene(scene);
        // stage.show();

        // stage.setTitle("Record");
        // stage.setResizable(false);
        // stage.setMinHeight(600.0);
        // stage.setMinWidth(400.0);

        // mainLayout.setPrefSize(400.0, 600.0);

        // scrollPane.setPrefSize(385, 535);
        // scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // scrollPane.setVvalue(1.0);
        // scrollPane.setFitToWidth(true);

        // dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);

        // userInput.setPrefWidth(325.0);

        // sendButton.setPrefWidth(55.0);

        // AnchorPane.setTopAnchor(scrollPane, 1.0);

        // AnchorPane.setBottomAnchor(sendButton, 1.0);
        // AnchorPane.setRightAnchor(sendButton, 1.0);

        // AnchorPane.setLeftAnchor(userInput, 1.0);
        // AnchorPane.setBottomAnchor(userInput, 1.0);
        // // -----End Part 2a-----

        // // -----Part 2b-----
        // //Handling user input

        // sendButton.setOnMouseClicked((event) -> {
        //     handleUserInput();
        // });
        // userInput.setOnAction((event) -> {
        //     handleUserInput();
        // });
        // dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));

        // -----End Part 2b-----


        // -----Part 4-----
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RecordGui.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindowController>getController().setRecord(record); // inject the Record instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // -----End Part 4------
    }

    /**
     * Creates a dialog box containing user input, and appends it to
     * the dialog container. Clears the user input after processing.
     */
    private void handleUserInput() {
        // -----Part 2a-----
        // dialogContainer.getChildren().addAll(new DialogBox(userInput.getText(), userImage));
        // userInput.clear();
        // -----End Part 2a-----

        // -----Part 2c-----
        String userText = userInput.getText();
        String recordText = record.getResponse(userInput.getText());
        dialogContainer.getChildren().addAll(
                DialogBoxController.getUserDialog(userText, userImage),
                DialogBoxController.getRecordDialog(recordText, recordImage)
        );
        userInput.clear();
        // -----End Part 2c-----
    }

    /**
     * Creates the main layout of the application.
     *
     * @return the root layout
     */
    private BorderPane createRoot() {
        BorderPane root = new BorderPane();

        root.setPadding(new Insets(10));

        VBox conversationArea = createConversationArea();
        VBox inputArea = createInputArea();

        root.setCenter(conversationArea);
        root.setBottom(inputArea);

        return root;
    }

    /**
     * Creates the conversation area.
     *
     * <p>The upper section contains user messages. A placeholder
     * area is reserved below it for the eventual Record application
     * responses.</p>
     *
     * @return the conversation area
     */
    private VBox createConversationArea() {
        VBox conversationArea = new VBox(10);

        VBox userSection = new VBox(5);
        userSection.setAlignment(Pos.TOP_RIGHT);

        Label userLabel = new Label("User");

        userMessageContainer = new VBox(5);
        userMessageContainer.setAlignment(Pos.TOP_RIGHT);

        ScrollPane userScrollPane = new ScrollPane(userMessageContainer);
        userScrollPane.setFitToWidth(true);
        userScrollPane.setFitToHeight(true);

        VBox.setVgrow(userScrollPane, Priority.ALWAYS);

        userSection.getChildren().addAll(userLabel, userScrollPane);

        VBox outputSection = new VBox(5);

        Label outputLabel = new Label("Record");

        ScrollPane outputScrollPane = new ScrollPane();
        outputScrollPane.setFitToWidth(true);
        outputScrollPane.setFitToHeight(true);

        VBox.setVgrow(outputScrollPane, Priority.ALWAYS);

        outputSection.getChildren().addAll(outputLabel, outputScrollPane);

        VBox.setVgrow(userSection, Priority.ALWAYS);
        VBox.setVgrow(outputSection, Priority.ALWAYS);

        conversationArea.getChildren().addAll(userSection, outputSection);

        return conversationArea;
    }

    /**
     * Creates the input area at the bottom of the window.
     *
     * @return the input area
     */
    private VBox createInputArea() {
        VBox inputArea = new VBox(5);

        userInputArea = new TextArea();
        userInputArea.setPromptText("Enter a command...");
        userInputArea.setWrapText(true);
        userInputArea.setPrefRowCount(3);

        Button sendButton = new Button("Send");

        sendButton.setOnAction(event -> submitUserInput());

        userInputArea.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    if (!event.isShiftDown()) {
                        event.consume();
                        submitUserInput();
                    }
                    break;
                default:
                    break;
            }
        });

        HBox buttonArea = new HBox(sendButton);
        buttonArea.setAlignment(Pos.CENTER_RIGHT);

        inputArea.getChildren().addAll(userInputArea, buttonArea);

        return inputArea;
    }

    /**
     * Submits the current user input.
     *
     * <p>The submitted text is currently displayed in the upper-right
     * user conversation area. Record processing and output handling
     * can be added later.</p>
     */
    private void submitUserInput() {
        String input = userInputArea.getText().trim();

        if (input.isEmpty()) {
            return;
        }

        Label message = new Label(input);
        message.setWrapText(true);

        userMessageContainer.getChildren().add(message);

        userInputArea.clear();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
