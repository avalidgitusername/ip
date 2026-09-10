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
import recordbase.types.ListItem;
import recordbase.types.RecordList;
import recordbase.types.ToDoItem;

/**
 * Provides methods for saving and loading {@code ListItem} objects to and from files.
 *
 * <p>The class handles conversion between list items and their file-based representation.</p>
 */
public class Storage {
    private static final int ITEM_TYPE_INDEX = 0;
    private static final int COMPLETION_STATUS_INDEX = 3;
    private static final char COMPLETED_STATUS = '1';
    private static final String QUOTED_FIELD_PREFIX = ", '";
    private static final String QUOTED_FIELD_SEPARATOR = "', ";
    private static final String CLOSING_QUOTE = "'";

    /**
     * Saves all items in the specified list to a file.
     *
     * @param list the list whose items are saved
     * @param fileName the name of the file to save the list to
     * @throws RecordException if the file cannot be created or written to
     */
    public static void saveToFile(RecordList list, String fileName) {
        assert list != null : "List must not be null";
        assert fileName != null : "File name must not be null";

        Path path = Paths.get(fileName);
        Path parentDirectory = path.getParent();

        try {
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (ListItem item : list.getItems()) {
                    assert item != null : "List must not contain null items";

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
    public static void loadFromFile(RecordList list, String fileName) {
        assert list != null : "List must not be null";
        assert fileName != null : "File name must not be null";

        Path path = Paths.get(fileName);

        if (Files.notExists(path)) {
            throw new RecordException("No save file to load from.");
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    assert line != null : "Saved line must not be null";

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
        char itemType = line.charAt(ITEM_TYPE_INDEX);
        boolean isDone = line.charAt(COMPLETION_STATUS_INDEX) == COMPLETED_STATUS;

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
        String[] fields = extractQuotedFields(line, 1);
        return new ToDoItem(fields[0]);
    }

    /**
     * Parses a saved deadline item from a line in the save file.
     *
     * @param line the line representing the saved deadline item
     * @return the parsed {@code DeadlineItem}
     */
    private static ListItem parseDeadlineItem(String line) {
        String[] fields = extractQuotedFields(line, 2);
        String task = fields[0];
        LocalDateTime deadline = LocalDateTime.parse(fields[1]);
        return new DeadlineItem(task, deadline);
    }

    /**
     * Parses a saved event item from a line in the save file.
     *
     * @param line the line representing the saved event item
     * @return the parsed {@code EventItem}
     */
    private static ListItem parseEventItem(String line) {
        String[] fields = extractQuotedFields(line, 3);
        String task = fields[0];
        LocalDateTime startDateTime = LocalDateTime.parse(fields[1]);
        LocalDateTime endDateTime = LocalDateTime.parse(fields[2]);
        return new EventItem(task, startDateTime, endDateTime);
    }

    /**
     * Extracts the quoted fields from a saved item in their stored order.
     *
     * @param line the saved item line
     * @param fieldCount the number of quoted fields expected in the line
     * @return the extracted field values
     */
    private static String[] extractQuotedFields(String line, int fieldCount) {
        String[] fields = new String[fieldCount];
        int searchStart = 0;

        for (int fieldIndex = 0; fieldIndex < fieldCount; fieldIndex++) {
            int fieldStart = line.indexOf(QUOTED_FIELD_PREFIX, searchStart)
                    + QUOTED_FIELD_PREFIX.length();
            boolean isLastField = fieldIndex == fieldCount - 1;
            int fieldEnd = isLastField
                    ? line.lastIndexOf(CLOSING_QUOTE)
                    : line.indexOf(QUOTED_FIELD_SEPARATOR, fieldStart);

            fields[fieldIndex] = line.substring(fieldStart, fieldEnd);
            searchStart = fieldEnd;
        }

        return fields;
    }
}
