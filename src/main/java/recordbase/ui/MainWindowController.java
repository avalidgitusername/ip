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

    private Record record;
    private ArrayList<String> pastMessages = new ArrayList();
    private int pastMessagesIndex = pastMessages.size();

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/SmallLogo.png"));
    private Image recordProfileImage = new Image(this.getClass().getResourceAsStream("/images/SmallLogo.png"));

    /**
     * Initializes the controller for the main ui of Record Application.
     */
    @FXML
    public void initialize() {
        assert scrollPane != null : "Scroll pane must be injected by FXML";
        assert dialogContainer != null : "Dialog container must be injected by FXML";
        assert userInput != null : "User input field must be injected by FXML";
        assert sendButton != null : "Send button must be injected by FXML";

        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        // Open old list records.
        Record.retrieveList("data/listdata.txt");

        dialogContainer.getChildren().addAll(
            DialogBoxController.getRecordDialog(Record.greet(), recordProfileImage)
        );

    }

    /** Injects the Record instance */
    @FXML
    public void setRecord(Record r) {
        assert r != null : "Record instance must not be null";

        record = r;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Record's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleSendUserInput() {
        assert userInput != null : "User input field must be injected by FXML";
        assert dialogContainer != null : "Dialog container must be injected by FXML";

        String input = userInput.getText();

        assert input != null : "User input text must not be null";

        if (input.length() == 0) {
            // Do nothing
            return;
        }

        String response;

        try {
            // Add current input into the list of past inputs if not immediately repeating
            if (pastMessages.size() > 0
                    && input.equalsIgnoreCase(pastMessages.get(pastMessages.size() - 1)) == false) {
                System.out.println("Adding new item2");
                pastMessages.add(input);
            } else if (pastMessages.size() == 0) {
                System.out.println("Adding new item1");
                pastMessages.add(input);
            }
            pastMessagesIndex = pastMessages.size();

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
        assert dialogContainer != null : "Dialog container must be injected by FXML";

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
        assert event != null : "Key event must not be null";

        // System.out.println("Handling action");
        switch (event.getCode()) {
            case KeyCode.UP -> {
                showPreviousUserInput();
            }
            case KeyCode.DOWN -> {
                showNextUserInput();
            }
            case KeyCode.ENTER -> {
                // Do nothing
            }
            default -> {
                // Do nothing
            }
        }
    }

    /**
     * Retrieves and displays the user's previous input.
     */
    @FXML
    private void showPreviousUserInput() {
        assert pastMessagesIndex >= 0 && pastMessagesIndex <= pastMessages.size()
                : "Past message index must remain within valid bounds";

        if (pastMessagesIndex > 0) {
            pastMessagesIndex--;
            userInput.setText(pastMessages.get(pastMessagesIndex));
        }
    }

    /**
     * Retrieves and displays the user's next input.
     */
    @FXML
    private void showNextUserInput() {
        assert pastMessagesIndex >= 0 && pastMessagesIndex <= pastMessages.size()
                : "Past message index must remain within valid bounds";

        if (pastMessagesIndex < pastMessages.size() - 1) {
            pastMessagesIndex++;
            userInput.setText(pastMessages.get(pastMessagesIndex));
        } else if (pastMessagesIndex == pastMessages.size() - 1) {
            pastMessagesIndex++;
            userInput.clear();
        }
    }
}
