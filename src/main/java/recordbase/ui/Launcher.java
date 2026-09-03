package recordbase.ui;

import javafx.application.Application;

/**
 * A launcher class to workaround classpath issues.
 */
public class Launcher {

    /**
     * Starting point to launch the Record Application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(RecordGui.class, args);
    }
}
