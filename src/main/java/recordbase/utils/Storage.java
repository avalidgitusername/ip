package recordbase.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import recordbase.exceptions.RecordException;
import recordbase.types.DeadlineItem;
import recordbase.types.EventItem;
import recordbase.types.ListItem;
import recordbase.types.Priority;
import recordbase.types.RecordList;
import recordbase.types.ToDoItem;

/**
 * Provides methods for saving and loading {@code ListItem} objects to and from files.
 *
 * <p>The class handles conversion between list items and their file-based representation.</p>
 */
public class Storage {
    private static final long MAX_SAVE_FILE_BYTES = 256L * 1024 * 1024;
    private static final int ITEM_TYPE_INDEX = 0;
    private static final int COMPLETION_STATUS_INDEX = 3;
    private static final char COMPLETED_STATUS = '1';
    private static final String QUOTED_FIELD_PREFIX = ", '";
    private static final String QUOTED_FIELD_SEPARATOR = "', ";
    private static final String CLOSING_QUOTE = "'";

    /**
     * Creates a storage utility instance.
     *
     * <p>Persistence methods are static. This constructor preserves the class's original public
     * construction contract.</p>
     */
    public Storage() { }

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

        Path path = resolvePath(fileName);
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
        } catch (IOException | SecurityException e) {
            throw new RecordException("Unable to save the task list to " + path
                    + ". Check that the location is writable.", e);
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

        Path path = resolvePath(fileName);

        if (Files.notExists(path)) {
            throw new RecordException("No save file to load from.");
        }

        try {
            if (!Files.isRegularFile(path) || !Files.isReadable(path)) {
                throw new RecordException("The save path is not a readable file: " + path);
            }
            if (Files.size(path) > MAX_SAVE_FILE_BYTES) {
                throw new RecordException("The save file is too large to load safely (maximum 256 MB).");
            }
        } catch (IOException | SecurityException exception) {
            throw new RecordException("Unable to inspect the save file. Check its permissions.", exception);
        }

        List<ListItem> loadedItems = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!line.isBlank()) {
                    if (loadedItems.size() >= RecordList.MAX_ITEMS) {
                        throw new RecordException("The save file contains more than "
                                + RecordList.MAX_ITEMS + " tasks. No tasks were loaded.");
                    }
                    try {
                        loadedItems.add(parseItem(line));
                    } catch (RuntimeException exception) {
                        throw new RecordException("The save file is corrupted or uses an unsupported format"
                                + " at line " + lineNumber + ". No tasks were loaded.", exception);
                    }
                }
            }
            for (ListItem item : loadedItems) {
                list.addItem(item);
            }
        } catch (IOException | SecurityException e) {
            throw new RecordException("Unable to read the save file. Check its permissions.", e);
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
        if (!line.matches("^[TDE], [01], (?:[1-5], )?'.*'$")) {
            throw new RecordException("Malformed saved item.");
        }
        char itemType = line.charAt(ITEM_TYPE_INDEX);
        boolean isDone = line.charAt(COMPLETION_STATUS_INDEX) == COMPLETED_STATUS;
        Priority priority = parsePriority(line);

        ListItem item;

        switch (itemType) {
            case 'T' -> {
                item = parseToDoItem(line, priority);
            }
            case 'D' -> {
                item = parseDeadlineItem(line, priority);
            }
            case 'E' -> {
                item = parseEventItem(line, priority);
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
     * Parses the priority stored after the completion flag.
     * Legacy records without a priority are treated as medium priority.
     *
     * @param line the line containing the priority of the item
     * @return the {@code Priority} represented by the line
     * @throws RecordException if the priority could not be extracted
     */
    private static Priority parsePriority(String line) {
        String remainder = line.substring(6);
        if (remainder.startsWith("'")) {
            return Priority.MEDIUM;
        }
        int separatorIndex = remainder.indexOf(',');
        if (separatorIndex == -1) {
            throw new RecordException("Saved item has no task description.");
        }
        return Priority.fromString(remainder.substring(0, separatorIndex));
    }

    /**
     * Parses a saved to-do item from a line in the save file.
     *
     * @param line the line representing the saved to-do item
     * @return the parsed {@code ToDoItem}
     */
    private static ListItem parseToDoItem(String line, Priority priority) {
        boolean hasScheduledDate = line.indexOf(QUOTED_FIELD_SEPARATOR) >= 0;
        String[] fields = extractQuotedFields(line, hasScheduledDate ? 2 : 1);
        return hasScheduledDate
                ? new ToDoItem(fields[0], LocalDateTime.parse(fields[1]), priority)
                : new ToDoItem(fields[0], priority);
    }

    /**
     * Parses a saved deadline item from a line in the save file.
     *
     * @param line the line representing the saved deadline item
     * @return the parsed {@code DeadlineItem}
     */
    private static ListItem parseDeadlineItem(String line, Priority priority) {
        String[] fields = extractQuotedFields(line, 2);
        String task = fields[0];
        LocalDateTime deadline = LocalDateTime.parse(fields[1]);
        return new DeadlineItem(task, deadline, priority);
    }

    /**
     * Parses a saved event item from a line in the save file.
     *
     * @param line the line representing the saved event item
     * @return the parsed {@code EventItem}
     */
    private static ListItem parseEventItem(String line, Priority priority) {
        String[] fields = extractQuotedFields(line, 3);
        String task = fields[0];
        LocalDateTime startDateTime = LocalDateTime.parse(fields[1]);
        LocalDateTime endDateTime = LocalDateTime.parse(fields[2]);
        return new EventItem(task, startDateTime, endDateTime, priority);
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

            if (fieldStart < QUOTED_FIELD_PREFIX.length() || fieldEnd < fieldStart) {
                throw new RecordException("Saved item has missing or malformed fields.");
            }

            fields[fieldIndex] = line.substring(fieldStart, fieldEnd);
            searchStart = fieldEnd;
        }

        return fields;
    }

    /**
     * Resolves and normalizes a storage path.
     *
     * <p>Relative paths are resolved against the application's current working directory.
     * Normalization also handles current-folder and parent-folder path segments.</p>
     *
     * @param fileName user-supplied absolute or relative path
     * @return normalized absolute path
     * @throws RecordException if the path is blank or cannot be parsed
     */
    private static Path resolvePath(String fileName) {
        if (fileName.isBlank()) {
            throw new RecordException("Please provide a file path.");
        }
        try {
            return Paths.get(fileName).toAbsolutePath().normalize();
        } catch (RuntimeException exception) {
            throw new RecordException("The file path is invalid.", exception);
        }
    }
}
