package recordbase.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import recordbase.exceptions.RecordException;
import recordbase.types.List;

/**
 * Provides methods for parsing user commands into {@code ListItem} objects.
 *
 * <p>The parser validates commands formats and extracts task details, dates, and times before adding
 * the corresponding items into a {@code List}.</p>
 */
public class ListParser {
    // Patterns generated using AI.
    private static final Pattern TODO_PATTERN = Pattern.compile(
            "\\Atodo[ \\t]+(?<task>.+?)[ \\t]*\\z"
    );

    private static final Pattern DEADLINE_PATTERN = Pattern.compile(
            "\\Adeadline[ \\t]+(?<task>.+?)[ \\t]+/by[ \\t]+"
            + "(?<byDate>\\d{8})"
            + "(?:[ \\t]+(?<byTime>\\d{2}:\\d{2}))?"
            + "\\z"
    );

    private static final Pattern EVENT_PATTERN = Pattern.compile(
            "\\Aevent[ \\t]+(?<task>.+?)[ \\t]+/from[ \\t]+"
            + "(?<fromDate>\\d{8})"
            + "(?:[ \\t]+(?<fromTime>\\d{2}:\\d{2}))?"
            + "[ \\t]+/to[ \\t]+"
            + "(?<toDate>\\d{8})"
            + "(?:[ \\t]+(?<toTime>\\d{2}:\\d{2}))?"
            + "\\z"
    );

    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("uuuuMMdd")
                .withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter TIME_FORMATTER =
        DateTimeFormatter.ofPattern("HH:mm")
                .withResolverStyle(ResolverStyle.STRICT);

    private static LocalDateTime parseDateTime(String dateText, String timeText) {
        LocalDate date = LocalDate.parse(dateText, DATE_FORMATTER);

        if (timeText == null) {
            return date.atStartOfDay();
        }

        LocalTime time = LocalTime.parse(timeText, TIME_FORMATTER);

        return LocalDateTime.of(date, time);
    }

    /**
     * Creates a {@code ToDoItem} from a properly formatted command and adds it to the specified list.
     *
     * @param command the command containing the task description
     * @param list the list to which the new to-do item is added
     * @return the index of the newly created item
     * @throws RecordException if the command is not properly formatted
     */
    public static int createListToDoFromLocalDT(String command, List list) {
        System.out.print(String.format("Parsing: %s", command));
        Matcher matcher = TODO_PATTERN.matcher(command);

        if (!matcher.matches()) {
            throw new RecordException("Dates should be in \"yyyymmdd [hh:mm]\"");
        }
        String task = matcher.group("task");

        return list.addToDoItem(task);
    }

    /**
     * Creates a {@code DeadlineItem} from a properly formatted command and adds it to the specified list.
     *
     * @param command the command containing the task description and deadline
     * @param list the list to which the new deadline item is added
     * @return the index of the newly created item
     * @throws RecordException if the command is not properly formatted
     */
    public static int createListDeadlineFromLocalDT(String command, List list) {
        Matcher matcher = DEADLINE_PATTERN.matcher(command);

        if (!matcher.matches()) {
            // System.out.println("Invalid Deadline command.");
            throw new RecordException("Dates should be in \"yyyymmdd [hh:mm]\"");
        }
        String task = matcher.group("task");
        LocalDateTime byDT = parseDateTime(matcher.group("byDate"), matcher.group("byTime"));

        return list.addDeadlineItem(task, byDT);
    }

    /**
     * Creates a {@code EventItem} from a properly formatted command and adds it to the specified list.
     *
     * @param command the command containing the task description and event times
     * @param list the list to which the new event item is added
     * @return the index of the newly created item
     * @throws RecordException if the command is not properly formatted
     */
    public static int createListEventFromLocalDT(String command, List list) {
        Matcher matcher = EVENT_PATTERN.matcher(command);

        if (!matcher.matches()) {
            throw new RecordException("Dates should be in \"yyyymmdd [hh:mm]\"");
        }
        String task = matcher.group("task");

        LocalDateTime fromDT = parseDateTime(matcher.group("fromDate"), matcher.group("fromTime"));
        LocalDateTime toDT = parseDateTime(matcher.group("toDate"), matcher.group("toTime"));

        return list.addEventItem(task, fromDT, toDT);
    }
}
