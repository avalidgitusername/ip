package recordbase.ui;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import recordbase.Record;
import recordbase.exceptions.RecordException;

/**
 * Controller for the main GUI.
 */
public class MainWindowController extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private final ArrayList<String> commandHistory = new ArrayList<>();
    private int commandHistoryIndex = commandHistory.size();

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/SmallLogo.png"));
    private final Image recordProfileImage = new Image(this.getClass().getResourceAsStream("/images/SmallLogo.png"));

    /**
     * Initializes the controller for the main ui of Record Application.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        // Open old list records.
        Record.retrieveList("data/listdata.txt");

        dialogContainer.getChildren().addAll(
            DialogBoxController.getRecordDialog(Record.greet(), recordProfileImage)
        );

    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Record's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleSendUserInput() {
        String input = userInput.getText();

        if (input.isEmpty()) {
            return;
        }

        String response;

        try {
            if (commandHistory.isEmpty()
                    || !input.equalsIgnoreCase(commandHistory.get(commandHistory.size() - 1))) {
                commandHistory.add(input);
            }
            commandHistoryIndex = commandHistory.size();

            response = Record.parseInput(input);
        } catch (RecordException e) {
            response = e.getMessage();
        }

        if (response == null) {
            dialogContainer.getChildren().addAll(
                DialogBoxController.getRecordDialog(Record.goodbye(), recordProfileImage)
            );
            Record.saveList("data/listdata.txt");
            Platform.exit();
            return;
        }

        dialogContainer.getChildren().addAll(
                DialogBoxController.getUserDialog(input, userImage),
                DialogBoxController.getRecordDialog(response, recordProfileImage)
        );
        userInput.clear();
    }

    /**
     * Handler to list all items in the current list.
     */
    @FXML
    private void handleButtonListItems() {
        dialogContainer.getChildren().addAll(
            DialogBoxController.getRecordDialog(Record.parseInput("list"), recordProfileImage)
        );
    }

    /**
     * Handler for Up and Down keypress in user input textbox.
     * Allows retrieval of past user inputs.
     */
    @FXML
    private void handleUpDownKeyPress(KeyEvent event) {

        switch (event.getCode()) {
            case KeyCode.UP -> {
                showPreviousUserInput();
            }
            case KeyCode.DOWN -> {
                showNextUserInput();
            }
            default -> { }
        }
    }

    /**
     * Retrieves and displays the user's previous input.
     */
    @FXML
    private void showPreviousUserInput() {
        if (commandHistoryIndex > 0) {
            commandHistoryIndex--;
            userInput.setText(commandHistory.get(commandHistoryIndex));
        }
    }

    /**
     * Retrieves and displays the user's next input.
     */
    @FXML
    private void showNextUserInput() {
        if (commandHistoryIndex < commandHistory.size() - 1) {
            commandHistoryIndex++;
            userInput.setText(commandHistory.get(commandHistoryIndex));
        } else if (commandHistoryIndex == commandHistory.size() - 1) {
            commandHistoryIndex++;
            userInput.clear();
        }
    }
}
