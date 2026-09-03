package recordbase;

import java.util.Scanner;

import recordbase.exceptions.RecordException;
import recordbase.types.List;
import recordbase.utils.ListParser;
import recordbase.utils.Storage;

/**
 * Provides the main entry point and user interface for the Record application.
 *
 * <p>The class handles user interactions, command processing, list management,
 * and loading and saving the task list.</p>
 */
public class Record {
    private static List list;

    /**
     * Displays the greeting banner and introductory message for the Record service.
     */
    public static String greet() {
        String dashBreak = "----------------------------------------\n";
        // Note banners have newline characters separated for ease of modification in escaped characters.
        String banner = "______                       _ " + "\n"
                        + "| ___ \\                     | |" + "\n"
                        + "| |_/ /___  ___ ___  _ __ __| |" + "\n"
                        + "|    // _ \\/ __/ _ \\| '__/ _` |" + "\n"
                        + "| |\\ \\  __/ (_| (_) | | | (_| |" + "\n"
                        + "\\_| \\_\\___|\\___\\___/|_|  \\__,_|" + "\n";
        System.out.println(banner);
        System.out.println(""); // Empty line for banner spacing

        StringBuilder sb = new StringBuilder();
        sb.append("Hello. You've connected to the Record service. How may I help.\n");
        sb.append(dashBreak);

        System.out.println(sb);

        return sb.toString();
    }

    /**
     * Displays the goodbye message when exiting the Record service.
     */
    public static String goodbye() {
        String dashBreak = "----------------------------------------\n";

        StringBuilder sb = new StringBuilder();
        sb.append("We hope you've fully Record-ed down everything needed! Goodbye!\n");
        sb.append(dashBreak);

        System.out.println(sb);

        return sb.toString();
    }

    /**
     * Prints and returns a message acknowledging the specifiec string has been recorded.
     *
     * @param str the string to display in the confirmation message
     * @return the acknowledgement message
     */
    public static String echoNoted(String str) {
        String tempStr = String.format("> Noted. I've recorded down: %s", str);
        System.out.println(tempStr);

        return tempStr;
    }

    /**
     * Displays the message to ask for user to input more data.
     */
    public static void echoAskInput() {
        String textAsk = "What else should I Record down?\n";
        System.out.println(textAsk);
    }

    /**
     * Parses the input as ...... TODO
     * @param input
     * @return
     */
    public static String parseInput(String input) {
        boolean isListItemOptionParsed = false;

        String inputLowercase = input.toLowerCase();
        System.out.println(String.format("Processing: %s", input));

        switch (inputLowercase) {
            case "" -> {
                // Do nothing. Assume user enter wrong.
                isListItemOptionParsed = true;
            }
            case "bye" -> {
                //shouldContinue = false;
                isListItemOptionParsed = true;
                return null;
            }
            case "list" -> {
                if (list != null) {
                    // System.out.println(list.toString());
                    return list.toString();
                } else {
                    throw new RecordException("No items in list!");
                    // System.out.println("No items in list!");
                }
                // isListItemOptionParsed = true;
                // return "No items in list!";
            }
            default -> {
                // Assume wrong input. Restart to asking.
                isListItemOptionParsed = false;
            }
        }

        // Handle marking of items in List.
        if (inputLowercase.startsWith("mark ")) {
            try {
                int index = Integer.parseInt(input.substring(5)) - 1;

                if (list != null) {
                    return list.setListItemDone(index);
                }

                // isListItemOptionParsed = true;
                // return "";
            } catch (NumberFormatException e) {
                // Ignore as nothing.
            }
        }

        if (inputLowercase.startsWith("unmark ")) {
            try {
                int index = Integer.parseInt(input.substring(7)) - 1;

                // Handle error
                if (list != null) {
                    return list.setListItemNotDone(index);
                }

                // isListItemOptionParsed = true;
            } catch (NumberFormatException e) {
                // Ignore as nothing.
            }
        }

        if (inputLowercase.startsWith("delete ")) {
            try {
                int index = Integer.parseInt(input.substring(7)) - 1;

                // Handle error
                if (list != null) {
                    String s1 = list.deleteItem(index);
                    // System.out.println(String.format("Success! Deleted: %s", s1));
                    return String.format("Success! Deleted: %s", s1);
                } else {
                    System.err.println("Error in Delete: No such item.");
                    throw new RecordException("Error in Delete: No such item.");
                }

                // isListItemOptionParsed = true;
                // return "Error in Delete: No such item.";
            } catch (NumberFormatException e) {
                // Ignore as nothing.
            }
        }
        // Create a new List object for this input
        if (isListItemOptionParsed == false) {
            if (Record.list == null) {
                Record.list = new List();
            }
            if (inputLowercase.startsWith("todo ")) {
                String task = input.substring(5).strip();

                if (task.length() == 0) {
                    throw new RecordException("ListItem description of item is not given.");
                }

                int listIndex = ListParser.createListToDoFromLocalDT(input, list);
                if (listIndex != -1) {
                    return echoNoted(list.getItem(listIndex).toString());
                }
            } else if (inputLowercase.startsWith("deadline ")) {
                int startSearchIndex = input.toLowerCase().indexOf("/by ");
                // Error handling
                // >1 "by" date
                if (startSearchIndex != inputLowercase.lastIndexOf("/by")) {
                    // System.err.println("Error in Deadline: More than 1 'by' date specified.");
                    throw new RecordException("Error in Deadline: More than 1 'by' date specified.");
                }

                if (startSearchIndex == -1) {
                    throw new RecordException("Error in Deadline: 'By' date not specified.");
                }

                String task = input.substring(8, startSearchIndex).strip();
                if (task.length() == 0) {
                    throw new RecordException("ListItem description of item is not given.");
                }

                // String byDate = user_input.substring(startSearchIndex + 3).strip();

                int listIndex = ListParser.createListDeadlineFromLocalDT(input, list);
                if (listIndex != -1) {
                    return echoNoted(list.getItem(listIndex).toString());
                }
            } else if (inputLowercase.startsWith("event ")) {
                int startSearchFromDateIndex = input.toLowerCase().indexOf("/from ");
                // Error handling
                // >1 "from" date
                if (startSearchFromDateIndex != inputLowercase.lastIndexOf("/from")) {
                    // System.err.println("Error in Event: More than 1 'from' date specified.");
                    throw new RecordException("Error in Event: More than 1 'from' date specified.");
                }
                int startSearchToDateIndex = inputLowercase.indexOf("/to ");
                // >1 "to" date
                if (startSearchToDateIndex != inputLowercase.lastIndexOf("/to")) {
                    // System.err.println("Error in Event: More than 1 'to' date specified.");
                    throw new RecordException("Error in Event: More than 1 'to' date specified.");
                }

                if (startSearchFromDateIndex == -1 || startSearchToDateIndex == -1) {
                    throw new RecordException("Error in Event: Either 'From' or 'To' date not specified.");
                }

                if (startSearchToDateIndex < startSearchFromDateIndex) {
                    // System.err.println("Error in Event: 'To' date specified before 'From' date.");
                    throw new RecordException("Error in Event: 'To' date specified before 'From' date.");
                }

                String task = input.substring(5, startSearchFromDateIndex).strip();
                if (task.length() == 0) {
                    throw new RecordException("ListItem description of item is not given.");
                }

                int listIndex = ListParser.createListEventFromLocalDT(input, list);
                if (listIndex != -1) {
                    return echoNoted(list.getItem(listIndex).toString());
                }
            } else {
                //     list.addItem(user_input);
                //     echo_noted(user_input);
                // System.out.println("Sorry! No such command available. Please try again yeah.");
                return "Sorry! No such command available. Please try again yeah.";
            }
        }
        return ";";
    }

    /**
     * Continuously reads and processes commands entered by the user.
     *
     * <p>Supported commands include creating, listing, marking, unmarking, deleting, and exiting the list.</p>
     */
    public static void askInput() {
        boolean shouldContinue = true;
        // String textAsk = "What else should I Record down?\n";

        try (Scanner scanner = new Scanner(System.in)) {
            while (shouldContinue) {
                try {
                    // System.out.println(textAsk);
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
        Record.list = new List();
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
