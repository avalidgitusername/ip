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
    public DialogBoxController(String text, Image image) {
        // -----Part 2-----
        // text = new Label(s);
        // displayPicture = new ImageView(i);
        // this.getChildren().addAll(text, displayPicture);
        // text = new Label(s);
        // displayPicture = new ImageView(i);
        // -----End Part 2-----

        // -----Part 2b-----
        //Styling the dialog box
        // text = new Label(s);
        // displayPicture = new ImageView(i);
        // text.setWrapText(true);
        // displayPicture.setFitWidth(100.0);
        // displayPicture.setFitHeight(100.0);
        // this.setAlignment(Pos.TOP_RIGHT);

        // this.getChildren().addAll(text, displayPicture);
        // ------End Part 2b-----

        // -----Part 4-----
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RecordGui.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(image);
        // -----End Part 4-----

    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }

    public static DialogBoxController getUserDialog(String s, Image i) {
        return new DialogBoxController(s, i);
    }

    public static DialogBoxController getRecordDialog(String s, Image i) {
        var db = new DialogBoxController(s, i);
        db.flip();
        return db;
    }
}
