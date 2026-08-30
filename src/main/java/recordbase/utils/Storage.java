package recordbase.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import recordbase.exceptions.RecordException;
import recordbase.types.DeadlineItem;
import recordbase.types.EventItem;
import recordbase.types.List;
import recordbase.types.ListItem;
import recordbase.types.ToDoItem;

/**
 * Provides methods for saving and loading {@code ListItem} objects to and from files.
 *
 * <p>The class handles conversion between list items and their file-based representation.</p>
 */
public class Storage {
    /**
     * Saves all items in the specified list to a file.
     *
     * @param list the list whose items are saved
     * @param fileName the name of the file to save the list to
     * @throws RecordException if the file cannot be created or written to
     */
    public static void saveToFile(List list, String fileName) {
        Path path = Paths.get(fileName);

        try {
            if (Files.notExists(path.getParent())) {
                try {
                    Files.createDirectories(path.getParent());
                } catch (IOException e) {
                    System.out.println("Unable to do something with creating directories");
                }
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (ListItem item : list.getItems()) {
                    writer.write(item.saveString().replaceAll("\'", "\\\'"));
                    writer.newLine();
                }
                writer.flush();
            }
        } catch (IOException e) {
            throw new RecordException("Unable to save list to file.");
        }
    }

    /**
     * Loads items from a file and adds them to the specified list.
     *
     * @param list the list to which the loaded items are added
     * @param fileName the name of the file to load from
     * @throws RecordException if the file does not exist or cannot be read
     */
    public static void loadFromFile(List list, String fileName) {
        Path path = Paths.get(fileName);

        if (Files.notExists(path)) {
            throw new RecordException("No save file to load from.");
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    ListItem item = parseItem(line);
                    list.addItem(item);
                }
            }
        } catch (IOException | RuntimeException e) {
            throw new RecordException("Unable to load list from file.", e);
        }
    }

    /**
     * Parses a line from a save file into a {@code ListItem}.
     *
     * @param line the line representing a saved list item
     * @return the {@code ListItem} represented by the line
     * @throws RecordException if the line contains an unknown item type
     */
    private static ListItem parseItem(String line) {
        char itemType = line.charAt(0);
        boolean isDone = line.charAt(3) == '1';

        ListItem item;

        switch (itemType) {
            case 'T' -> {
                item = parseToDoItem(line);
            }
            case 'D' -> {
                item = parseDeadlineItem(line);
            }
            case 'E' -> {
                item = parseEventItem(line);
            }
            default -> {
                throw new RecordException("Unknown item type: " + itemType);
            }
        }

        if (isDone) {
            item.setDone();
        }

        return item;
    }

    /**
     * Parses a saved to-do item from a line in the save file.
     *
     * @param line the line representing the saved to-do item
     * @return the parsed {@code ToDoItem}
     */
    private static ListItem parseToDoItem(String line) {
        int taskStart = line.indexOf(", '") + 3;
        int taskEnd = line.lastIndexOf("'");

        String task = line.substring(taskStart, taskEnd);

        return new ToDoItem(task);
    }

    /**
     * Parses a saved deadline item from a line in the save file.
     *
     * @param line the line representing the saved deadline item
     * @return the parsed {@code DeadlineItem}
     */
    private static ListItem parseDeadlineItem(String line) {
        int taskStart = line.indexOf(", '") + 3;
        int taskEnd = line.indexOf("', ", taskStart);

        int dateStart = line.indexOf(", '", taskEnd) + 3;
        int dateEnd = line.lastIndexOf("'");

        String task = line.substring(taskStart, taskEnd);
        LocalDateTime byDate =
                LocalDateTime.parse(line.substring(dateStart, dateEnd));

        return new DeadlineItem(task, byDate);
    }

    /**
     * Parses a saved event item from a line in the save file.
     *
     * @param line the line representing the saved event item
     * @return the parsed {@code EventItem}
     */
    private static ListItem parseEventItem(String line) {
        int taskStart = line.indexOf(", '") + 3;
        int taskEnd = line.indexOf("', ", taskStart);

        int fromDateStart = line.indexOf(", '", taskEnd) + 3;
        int fromDateEnd = line.indexOf("', ", fromDateStart);

        int toDateStart = line.indexOf(", '", fromDateEnd) + 3;
        int toDateEnd = line.lastIndexOf("'");

        String task = line.substring(taskStart, taskEnd);

        LocalDateTime fromDate =
                LocalDateTime.parse(line.substring(fromDateStart, fromDateEnd));

        LocalDateTime toDate =
                LocalDateTime.parse(line.substring(toDateStart, toDateEnd));

        return new EventItem(task, fromDate, toDate);
    }
}
