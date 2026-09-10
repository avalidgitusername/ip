package recordbase.types;

import java.util.Arrays;
import java.util.Locale;

/**
 * Represents a command supported by the Record application.
 */
public enum CommandType {
    BYE("bye"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    UNKNOWN("");

    private final String commandWord;

    /**
     * Creates a command type with its corresponding command word.
     *
     * @param commandWord the word used to invoke the command
     */
    CommandType(String commandWord) {
        this.commandWord = commandWord;
    }

    /**
     * Returns the command type represented by the given keyword.
     *
     * @param commandWord the keyword to interpret
     * @return the matching command {@code CommandType}, or {@code UNKNOWN}
     */
    public static CommandType fromString(String commandWord) {
        String normalizedCommand = commandWord.toLowerCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(command -> command.commandWord.equals(normalizedCommand))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
