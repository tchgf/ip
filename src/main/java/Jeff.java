import java.util.Scanner;

public class Jeff {
    private static final String INDENT = "    ";
    private static final String DIVIDER = INDENT + "____________________________________________________________";
    private static final int MAX_HISTORY = 100;

    /** Fixed-size store of every task the user has added so far, in order. */
    private static final Task[] history = new Task[MAX_HISTORY];
    private static int historyCount = 0;

    /**
     * Prints a (possibly multi-line) machine message with each line indented,
     * so it is visually distinct from the user's own typed input.
     */
    private static void printIndented(String message) {
        for (String line : message.split("\n")) {
            System.out.println(INDENT + line);
        }
    }

    /** Prints every stored task so far as a numbered list, in the order added. */
    private static void printHistory() {
        for (int i = 0; i < historyCount; i++) {
            printIndented((i + 1) + "." + history[i]);
        }
    }

    /** Returns the {@code history} index for the task number following a "mark"/"unmark" command word. */
    private static int parseTaskIndex(String input, String commandWord) {
        String numberPart = input.substring(commandWord.length()).trim();
        return Integer.parseInt(numberPart) - 1;
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
            System.out.println(DIVIDER);
            if (input.equals("bye")) {
                printIndented("Bye. Hope to see you again soon!");
                System.out.println(DIVIDER);
                break;
            }
            if (input.equals("list")) {
                printHistory();
                System.out.println(DIVIDER);
                continue;
            }
            if (input.startsWith("mark")) {
                Task task = history[parseTaskIndex(input, "mark")];
                task.markAsDone();
                printIndented("Nice! I've marked this task as done:");
                printIndented("  " + task);
                System.out.println(DIVIDER);
                continue;
            }
            if (input.startsWith("unmark")) {
                Task task = history[parseTaskIndex(input, "unmark")];
                task.unmarkAsDone();
                printIndented("OK, I've marked this task as not done yet:");
                printIndented("  " + task);
                System.out.println(DIVIDER);
                continue;
            }
            if (historyCount < MAX_HISTORY) {
                history[historyCount] = new Task(input);
                historyCount++;
            }
            printIndented("added: " + input);
            System.out.println(DIVIDER);
        }
        scanner.close();
    }
}
