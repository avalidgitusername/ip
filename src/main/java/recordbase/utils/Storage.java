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

public class Storage {
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

    private static ListItem parseToDoItem(String line) {
        int taskStart = line.indexOf(", '") + 3;
        int taskEnd = line.lastIndexOf("'");

        String task = line.substring(taskStart, taskEnd);

        return new ToDoItem(task);
    }

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
