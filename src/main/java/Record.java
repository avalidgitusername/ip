
public class Record {
    public static void greet() {
        String dash_break = "----------------------------------------\n";
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
        sb.append(dash_break);

        sb.append("We hope you've fully Record-ed down everything needed! Goodbye!\n");
        sb.append(dash_break);

        System.out.println(sb);


        
    }

    public static void main(String[] args) {
        greet();
    }
}
