import java.util.Scanner;

public class Jeff {
    private static final String INDENT = "    ";
    private static final String DIVIDER = INDENT + "____________________________________________________________";

    /**
     * Prints a (possibly multi-line) machine message with each line indented,
     * so it is visually distinct from the user's own typed input.
     */
    private static void printIndented(String message) {
        for (String line : message.split("\n")) {
            System.out.println(INDENT + line);
        }
    }

    public static void main(String[] args) {
        String banner = "     _  _____  _____  _____ \n"
                + "    | || ____||  ___||  ___|\n"
                + "    | || |__  | |_   | |_   \n"
                + "| |_| ||  __| |  _|  |  _|  \n"
                + " \\___/ |_____||_|    |_|    ";
        System.out.println(DIVIDER);
        printIndented(banner);
        printIndented("Hello! I'm Jeff.");
        printIndented("What can I do for you?");
        System.out.println(DIVIDER);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                printIndented("Bye. Hope to see you again soon!");
                System.out.println(DIVIDER);
                break;
            }
            printIndented(input);
            System.out.println(DIVIDER);
        }
        scanner.close();
    }
}
