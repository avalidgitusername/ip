package recordbase;

import java.util.Scanner;

import recordbase.exceptions.RecordException;
import recordbase.types.ParsedCommand;
import recordbase.types.RecordList;
import recordbase.utils.CommandParser;
import recordbase.utils.ListParser;
import recordbase.utils.Storage;

/**
 * Provides the main entry point and user interface for the Record application.
 *
 * <p>The class handles user interactions, command processing, list management,
 * and loading and saving the task list.</p>
 */
public class Record {
    private static RecordList list;

    /**
     * Displays the greeting banner and introductory message for the Record service.
     */
    public static String greet() {
        String separator = "----------------------------------------\n";
        // Note banners have newline characters separated for ease of modification in escaped characters.
        String banner = "______                       _ " + "\n"
                        + "| ___ \\                     | |" + "\n"
                        + "| |_/ /___  ___ ___  _ __ __| |" + "\n"
                        + "|    // _ \\/ __/ _ \\| '__/ _` |" + "\n"
                        + "| |\\ \\  __/ (_| (_) | | | (_| |" + "\n"
                        + "\\_| \\_\\___|\\___\\___/|_|  \\__,_|" + "\n";
        System.out.println(banner);
        System.out.println(""); // Empty line for banner spacing

        StringBuilder response = new StringBuilder();
        response.append("Hello. You've connected to the Record service. How may I help.\n");
        response.append(separator);

        System.out.println(response);

        return response.toString();
    }

    /**
     * Displays the goodbye message when exiting the Record service.
     */
    public static String goodbye() {
        String separator = "----------------------------------------\n";

        StringBuilder response = new StringBuilder();
        response.append("We hope you've fully Record-ed down everything needed! Goodbye!\n");
        response.append(separator);

        System.out.println(response);

        return response.toString();
    }

    /**
     * Prints and returns a message acknowledging the specifiec string has been recorded.
     *
     * @param notedItem the item description to display in the confirmation message
     * @return the acknowledgement message
     */
    public static String echoNoted(String notedItem) {
        String response = String.format("> Noted. I've recorded down: %s", notedItem);
        System.out.println(response);

        return response;
    }

    /**
     * Displays the message to ask for user to input more data.
     */
    public static void echoAskInput() {
        String textAsk = "What else should I Record down?\n";
        System.out.println(textAsk);
    }

    /**
     * Processes a user command and returns the corresponding application response.
     *
     * @param input the raw user command
     * @return the response, or {@code null} when the application should exit
     * @throws RecordException if the command or its arguments are invalid
     */
    public static String parseInput(String input) {
        ParsedCommand command = CommandParser.parse(input);
        return switch (command.type()) {
            case BYE -> parseByeCommand(command);
            case LIST -> parseListCommand(command);
            case MARK -> markItem(command.arguments());
            case UNMARK -> unmarkItem(command.arguments());
            case DELETE -> deleteItem(command.arguments());
            case TODO -> createToDo(command.arguments());
            case DEADLINE -> createDeadline(command.arguments());
            case EVENT -> createEvent(command.arguments());
            case UNKNOWN -> "Sorry! No such command available. Please try again yeah.";
        };

    }

    /**
     * Processes a bye command.
     *
     * @param command the parsed bye command
     * @return {@code null} to signal that the application should exit
     * @throws RecordException if arguments were supplied
     */
    private static String parseByeCommand(ParsedCommand command) {
        requireNoArguments(command);
        return null;
    }

    /**
     * Processes a list command.
     *
     * @param command the parsed list command
     * @return the current list contents
     * @throws RecordException if arguments were supplied
     */
    private static String parseListCommand(ParsedCommand command) {
        requireNoArguments(command);
        return list == null ? "No items in list!" : list.toString();
    }

    /**
     * Rejects unexpected arguments for commands that do not accept them.
     *
     * @param command the command to validate
     * @throws RecordException if arguments were supplied
     */
    private static void requireNoArguments(ParsedCommand command) {
        if (command.hasArguments()) {
            throw new RecordException("This command does not accept arguments.");
        }
    }

    /**
     * Marks the numbered item as completed.
     *
     * @param arguments the one-based item number
     * @return a confirmation containing the updated item
     */
    private static String markItem(String arguments) {
        return getOrCreateList().setListItemDone(parseItemIndex(arguments));
    }

    /**
     * Marks the numbered item as not completed.
     *
     * @param arguments the one-based item number
     * @return a confirmation containing the updated item
     */
    private static String unmarkItem(String arguments) {
        return getOrCreateList().setListItemNotDone(parseItemIndex(arguments));
    }

    /**
     * Deletes the numbered item.
     *
     * @param arguments the one-based item number
     * @return a confirmation containing the deleted item
     */
    private static String deleteItem(String arguments) {
        String deletedItem = getOrCreateList().deleteItem(parseItemIndex(arguments));
        return String.format("Success! Deleted: %s", deletedItem);
    }

    /**
     * Converts a one-based item number supplied by the user into a zero-based index.
     *
     * @param arguments the user-supplied item number
     * @return the zero-based item index
     * @throws RecordException if the argument is not a single positive integer
     */
    private static int parseItemIndex(String arguments) {
        try {
            return Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException exception) {
            throw new RecordException("Please provide a valid item number.", exception);
        }
    }

    /**
     * Creates a to-do item from command arguments.
     *
     * @param arguments the task description
     * @return the creation acknowledgement
     */
    private static String createToDo(String arguments) {
        int index = ListParser.parseToDo("todo " + arguments, getOrCreateList());
        return echoNoted(list.getItem(index).toString());
    }

    /**
     * Creates a deadline item from command arguments.
     *
     * @param arguments the task description and deadline
     * @return the creation acknowledgement
     */
    private static String createDeadline(String arguments) {
        int index = ListParser.parseDeadline("deadline " + arguments, getOrCreateList());
        return echoNoted(list.getItem(index).toString());
    }

    /**
     * Creates an event item from command arguments.
     *
     * @param arguments the task description and duration
     * @return the creation acknowledgement
     */
    private static String createEvent(String arguments) {
        int index = ListParser.parseEvent("event " + arguments, getOrCreateList());
        return echoNoted(list.getItem(index).toString());
    }

    /**
     * Returns the current list, creating an empty list when necessary.
     *
     * @return the current list
     */
    private static RecordList getOrCreateList() {
        if (list == null) {
            list = new RecordList();
        }
        return list;
    }

    /**
     * Continuously reads and processes commands entered by the user.
     *
     * <p>Supported commands include creating, listing, marking, unmarking, deleting, and exiting the list.</p>
     */
    public static void askInput() {
        boolean shouldContinue = true;
        try (Scanner scanner = new Scanner(System.in)) {
            while (shouldContinue) {
                try {
                    echoAskInput();
                    String userInput;

                    userInput = scanner.nextLine();

                    String output = parseInput(userInput);

                    if (output == null) {
                        shouldContinue = false;
                    } else {
                        System.out.println(output);
                    }


                } catch (RecordException recExpt) {
                    System.out.println(recExpt);
                }
            }
        }
    }

    /**
     * Saves the current list to the specified file.
     *
     * @param strPath the path of the file to save the list to
     */
    public static void saveList(String strPath) {
        if (Record.list != null) {
            Storage.saveToFile(Record.list, strPath);
        }
    }

    /**
     * Loads a list from the specified file and sets it as the current list.
     *
     * @param strPath the path of the file from which to load the list
     */
    public static void retrieveList(String strPath) {
        Record.list = new RecordList();
        try {
            Storage.loadFromFile(Record.list, strPath);
        } catch (RecordException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        return "Record-ded: " + input;
    }

    /**
     * Starts the Record application by displaying the greeting, loading the saved list, accepting user input,
     * saving the list, and displaying the goodbye message.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        greet();
        retrieveList("data/listdata.txt");
        askInput();
        saveList("data/listdata.txt");
        goodbye();
    }
}
