package recordbase.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
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
    private static final String WINDOW_ICON_PATH = "/images/RecordAvatar.png";

    /**
     * Starts the JavaFX application.P
     * @param stage the primary stage
     */
    @Override
    public void start(Stage stage) {
        assert stage != null : "Primary stage must not be null";

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RecordGui.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();

            assert ap != null : "Main window layout must be loaded";

            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle(Record.APP_NAME);
            // Windows scales this square source for both the title bar and taskbar.
            stage.getIcons().add(new Image(RecordGui.class.getResourceAsStream(WINDOW_ICON_PATH)));
            stage.setMinWidth(320);
            stage.setMinHeight(360);
            stage.setOnCloseRequest(event -> {
                Record.saveList("data/listdata.txt");
                Record.goodbye();
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
