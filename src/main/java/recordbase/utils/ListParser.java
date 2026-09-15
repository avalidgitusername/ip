package recordbase.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import recordbase.exceptions.RecordException;
import recordbase.types.Priority;
import recordbase.types.RecordList;

/** Parses task commands and their slash-prefixed options. */
public class ListParser {
    private static final Pattern TODO_DATE_PATTERN = Pattern.compile(
            "^(?<description>.*\\S)\\s+(?<date>\\d{8})(?:\\s+(?<time>\\d{2}:\\d{2}))?$"
    );
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    /** Holds a description and the options extracted from a command. */
    private record ParsedOptions(String description, Map<String, List<String>> values) { }

    /** Parses a to-do and adds it to the supplied list. */
    public static int parseToDo(String command, RecordList list) {
        assert command != null : "Command must not be null";
        assert list != null : "List must not be null";
        ParsedOptions parsed = parseOptions(command, "todo", Set.of("priority"));
        Matcher dateMatcher = TODO_DATE_PATTERN.matcher(parsed.description());
        if (dateMatcher.matches()) {
            LocalDateTime scheduledDate = parseDateTime(dateMatcher.group("date"), dateMatcher.group("time"));
            return list.addToDoItem(dateMatcher.group("description"), scheduledDate,
                    parsePriority(parsed.values().get("priority")));
        }
        return list.addToDoItem(parsed.description(), parsePriority(parsed.values().get("priority")));
    }

    /** Parses a deadline and adds it to the supplied list. */
    public static int parseDeadline(String command, RecordList list) {
        assert command != null : "Command must not be null";
        assert list != null : "List must not be null";
        ParsedOptions parsed = parseOptions(command, "deadline", Set.of("by", "priority"));
        LocalDateTime deadline = parseRequiredDateTime(parsed.values(), "by");
        return list.addDeadlineItem(parsed.description(), deadline,
                parsePriority(parsed.values().get("priority")));
    }

    /** Parses an event, accepting its options in any order, and adds it to the list. */
    public static int parseEvent(String command, RecordList list) {
        assert command != null : "Command must not be null";
        assert list != null : "List must not be null";
        ParsedOptions parsed = parseOptions(command, "event", Set.of("from", "to", "priority"));
        LocalDateTime from = parseRequiredDateTime(parsed.values(), "from");
        LocalDateTime to = parseRequiredDateTime(parsed.values(), "to");
        if (to.isBefore(from)) {
            throw new RecordException("The event end must not be before its start.");
        }
        return list.addEventItem(parsed.description(), from, to,
                parsePriority(parsed.values().get("priority")));
    }

    /** Separates free text from slash options, allowing options in any order. */
    private static ParsedOptions parseOptions(String command, String commandName, Set<String> allowedOptions) {
        String trimmed = command.trim();
        if (!trimmed.equals(commandName) && !trimmed.startsWith(commandName + " ")) {
            throw new RecordException("Invalid " + commandName + " command.");
        }
        String arguments = trimmed.substring(commandName.length()).trim();
        String[] tokens = arguments.isEmpty() ? new String[0] : arguments.split("\\s+");
        List<String> descriptionTokens = new ArrayList<>();
        Map<String, List<String>> optionValues = new HashMap<>();

        for (int index = 0; index < tokens.length;) {
            String token = tokens[index];
            if (!token.startsWith("/")) {
                descriptionTokens.add(token);
                index++;
                continue;
            }
            String option = token.substring(1).toLowerCase();
            if (option.isBlank() || !allowedOptions.contains(option)) {
                throw new RecordException("Unknown option: " + token);
            }
            if (optionValues.containsKey(option)) {
                throw new RecordException("Option /" + option + " was provided more than once.");
            }
            List<String> values = new ArrayList<>();
            index++;
            if (index < tokens.length && !tokens[index].startsWith("/")) {
                values.add(tokens[index++]);
            }
            if (!option.equals("priority") && index < tokens.length
                    && tokens[index].matches("\\d{2}:\\d{2}")) {
                values.add(tokens[index++]);
            }
            optionValues.put(option, values);
        }

        String description = String.join(" ", descriptionTokens).trim();
        if (description.isEmpty()) {
            throw new RecordException("Please provide a task description.");
        }
        return new ParsedOptions(description, optionValues);
    }

    /** Parses a required date option in {@code yyyymmdd [hh:mm]} format. */
    private static LocalDateTime parseRequiredDateTime(Map<String, List<String>> options, String name) {
        List<String> values = options.get(name);
        if (values == null) {
            throw new RecordException("Please provide /" + name + " in yyyymmdd [hh:mm] format.");
        }
        if (values.size() < 1 || values.size() > 2) {
            throw new RecordException("Option /" + name + " must use yyyymmdd [hh:mm].");
        }
        if (!values.get(0).matches("\\d{8}")
                || values.size() == 2 && !values.get(1).matches("\\d{2}:\\d{2}")) {
            throw new RecordException("Option /" + name + " must use yyyymmdd [hh:mm].");
        }
        return parseDateTime(values.get(0), values.size() == 2 ? values.get(1) : null);
    }

    /** Converts a validated compact date and optional time into a date-time value. */
    private static LocalDateTime parseDateTime(String dateText, String timeText) {
        try {
            LocalDate date = LocalDate.parse(dateText, DATE_FORMATTER);
            LocalTime time = timeText == null ? LocalTime.MIDNIGHT : LocalTime.parse(timeText, TIME_FORMATTER);
            return LocalDateTime.of(date, time);
        } catch (DateTimeParseException exception) {
            throw new RecordException("Invalid date or time. Use a real calendar date as yyyymmdd"
                    + " and an optional 24-hour time as hh:mm.", exception);
        }
    }

    /** Parses an optional one-value priority, defaulting to medium. */
    private static Priority parsePriority(List<String> values) {
        if (values == null) {
            return Priority.MEDIUM;
        }
        if (values.size() != 1) {
            throw new RecordException("Option /priority needs exactly one value.");
        }
        return Priority.fromString(values.get(0));
    }
}
