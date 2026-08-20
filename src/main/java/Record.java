import java.util.Scanner;

public class Record {
    // private static List[] taskList;
    // private static int taskCounter = 0;
    private static List list;

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
                System.out.println(text_ask);
                String user_input = scanner.nextLine();
                
                switch (user_input) {
                    case "" -> {
                        // Do nothing. Assume user enter wrong.
                    }
                    case "bye" -> {
                        b_cont = false;
                    }
                    case "list" -> {
                        if (list != null) {
                            System.out.println(list.toString());
                        } else {
                            System.out.println("No items in list!");
                        }
                    }
                    default -> {
                        // Create a new List object for this input
                        if (Record.list == null) {
                            Record.list = new List(100);
                        }
                        if (list.addItem(user_input)) {
                            echo_noted(user_input);
                        }
                    }
                }
            }
        } 
    }


    public static void main(String[] args) {
        greet();

        ask_input();
        goodbye();
    }
}
