package recordbase.ui;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Creates a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBoxController extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Creates a custom DialogBox view of a given string and an image.
     *
     * @param text the string for the image
     * @param image the image object itself to display
     */
    public DialogBoxController(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RecordGui.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();

            assert dialog != null : "Dialog label must be injected by FXML";
            assert displayPicture != null : "Display picture must be injected by FXML";

            dialog.setText(text);
            displayPicture.setImage(img);
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> childNodes = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(childNodes);
        this.getChildren().setAll(childNodes);
    }

    public static DialogBoxController getUserDialog(String text, Image img) {
        assert text != null : "User dialog text must not be null";
        assert img != null : "User dialog image must not be null";

        return new DialogBoxController(text, img);
    }

    public static DialogBoxController getRecordDialog(String text, Image img) {
        assert text != null : "Record dialog text must not be null";
        assert img != null : "Record dialog image must not be null";

        var dialogBox = new DialogBoxController(text, img);
        dialogBox.flip();
        return dialogBox;
    }
}
