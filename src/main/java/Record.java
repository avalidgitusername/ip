import java.util.Scanner;

public class Record {
    // private static List[] taskList;
    // private static int taskCounter = 0;
    private static List list;
    // private static ListParser parser = new ListParser();

    public static void greet() {
        String dashBreak = "----------------------------------------\n";
        // Note banners have newline characters separated for ease of modification in escaped characters.
        String banner = "______                       _ " + "\n" +
                        "| ___ \\                     | |" + "\n" +
                        "| |_/ /___  ___ ___  _ __ __| |" + "\n" +
                        "|    // _ \\/ __/ _ \\| '__/ _` |" + "\n" +
                        "| |\\ \\  __/ (_| (_) | | | (_| |" + "\n" +
                        "\\_| \\_\\___|\\___\\___/|_|  \\__,_|" + "\n" +
                        "                               "; // This line is meant to be empty for spacing.
        System.out.println(banner);
        
        StringBuilder sb = new StringBuilder();
        sb.append("Hello. You've connected to the Record service. How may I help.\n");
        sb.append(dashBreak);

        System.out.println(sb);
    }
    public static void goodbye() {
        String dashBreak = "----------------------------------------\n";

        StringBuilder sb = new StringBuilder();
        sb.append("We hope you've fully Record-ed down everything needed! Goodbye!\n");
        sb.append(dashBreak);

        System.out.println(sb);
    }

    public static void echo_noted(String str) {
        System.out.println("> Noted. I've recorded down: " + str);
    }

    public static void ask_input() {
        boolean b_cont = true;
        String text_ask = "What else should I Record down?\n";
        try (Scanner scanner = new Scanner(System.in)) {
            while (b_cont) {
                try {
                    System.out.println(text_ask);
                    String user_input = scanner.nextLine();

                    boolean listItemOptionParsed = false;
                    
                    switch (user_input) {
                        case "" -> {
                            // Do nothing. Assume user enter wrong.
                            listItemOptionParsed = true;
                        }
                        case "bye" -> {
                            b_cont = false;
                            listItemOptionParsed = true;
                        }
                        case "list" -> {
                            if (list != null) {
                                System.out.println(list.toString());
                            } else {
                                System.out.println("No items in list!");
                            }
                            listItemOptionParsed = true;
                        }
                    }

                    // Handle marking of items in List.
                    if (user_input.toLowerCase().startsWith("mark ")) {
                        try {
                            int index = Integer.parseInt(user_input.substring(5)) - 1;

                            if (list != null) {
                                list.setListItemDone(index);
                            }
                            
                            listItemOptionParsed = true;
                        } catch (NumberFormatException e) {
                            // Ignore as nothing.
                        }
                    }

                    if (user_input.toLowerCase().startsWith("unmark ")) {
                        try {
                            int index = Integer.parseInt(user_input.substring(7)) - 1;

                            // Handle error
                            if (list != null) {
                                list.setListItemNotDone(index);
                            }

                            listItemOptionParsed = true;
                        } catch (NumberFormatException e) {
                            // Ignore as nothing.
                        }
                    }

                    if (user_input.toLowerCase().startsWith("delete ")) {
                        try {
                            int index = Integer.parseInt(user_input.substring(7)) - 1;

                            // Handle error
                            if (list != null) {
                                String s1 = list.deleteItem(index);
                                System.out.println(String.format("Success! Deleted: %s",s1));
                            } else {
                                System.err.println("Error in Delete: No such item.");
                            }

                            listItemOptionParsed = true;
                        } catch (NumberFormatException e) {
                            // Ignore as nothing.
                        }
                    }
                    // Create a new List object for this input
                    if (listItemOptionParsed == false) {
                        if (Record.list == null) {
                            Record.list = new List();
                        }
                        if (user_input.toLowerCase().startsWith("todo ")) {
                            String task = user_input.substring(5).strip();

                            if (task.length() == 0) {
                                throw new RecordException("ListItem description of item is not given.");
                            }

                            int listIndex = ListParser.createListToDoFromLocalDT(user_input, list);
                            if (listIndex != -1) {
                                echo_noted(list.getItem(listIndex).toString());
                            }
                        } else if (user_input.toLowerCase().startsWith("deadline ")) {
                            int startSearchIndex = user_input.toLowerCase().indexOf("/by ");
                            // Error handling
                            // >1 "by" date
                            if (startSearchIndex != user_input.toLowerCase().lastIndexOf("/by")) {
                                // System.err.println("Error in Deadline: More than 1 'by' date specified.");
                                throw new RecordException("Error in Deadline: More than 1 'by' date specified.");
                            }

                            if (startSearchIndex == -1) {
                                throw new RecordException("Error in Deadline: 'By' date not specified.");
                            }

                            String task = user_input.substring(8, startSearchIndex).strip();
                            if (task.length() == 0) {
                                throw new RecordException("ListItem description of item is not given.");
                            }

                            // String byDate = user_input.substring(startSearchIndex + 3).strip();

                            int listIndex = ListParser.createListDeadlineFromLocalDT(user_input, list);
                            if (listIndex != -1) {
                                echo_noted(list.getItem(listIndex).toString());
                            }
                        } else if (user_input.toLowerCase().startsWith("event ")) {
                            int startSearchFromDateIndex = user_input.toLowerCase().indexOf("/from ");
                            // Error handling
                            // >1 "from" date
                            if (startSearchFromDateIndex != user_input.toLowerCase().lastIndexOf("/from")) {
                                // System.err.println("Error in Event: More than 1 'from' date specified.");
                                throw new RecordException("Error in Event: More than 1 'from' date specified.");
                            }
                            int startSearchToDateIndex = user_input.toLowerCase().indexOf("/to ");
                            // >1 "to" date
                            if (startSearchToDateIndex != user_input.toLowerCase().lastIndexOf("/to")) {
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


                            String task = user_input.substring(5, startSearchFromDateIndex).strip();
                            if (task.length() == 0) {
                                throw new RecordException("ListItem description of item is not given.");
                            }

                            int listIndex = ListParser.createListEventFromLocalDT(user_input, list);
                            if (listIndex != -1) {
                                echo_noted(list.getItem(listIndex).toString());
                            }
                        } 
                        else {
                        //     list.addItem(user_input);
                        //     echo_noted(user_input);
                            System.out.println("Sorry! No such command available. Please try again yeah.");
                        }
                    }
                } catch (RecordException recExpt) {
                    System.out.println(recExpt);
                }
            }
        }
    }

    public static void saveList(String strPath) {
        if (Record.list != null) {
            Storage.saveToFile(Record.list, strPath);
        }
    }

    public static void retrieveList(String strPath) {
        Record.list = new List();
        Storage.loadFromFile(Record.list, strPath);
    }

    public static void main(String[] args) {
        greet();
        retrieveList("data/listdata.txt");
        ask_input();
        saveList("data/listdata.txt");
        goodbye();
    }
}
