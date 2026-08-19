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
        printIndented("Here are the tasks in your list:");
        for (int i = 0; i < historyCount; i++) {
            printIndented((i + 1) + "." + history[i]);
        }
    }

    /** Returns the {@code history} index for the task number following a "mark"/"unmark" command word. */
    private static int parseTaskIndex(String input, String commandWord) {
        String numberPart = input.substring(commandWord.length()).trim();
        return Integer.parseInt(numberPart) - 1;
    }

    /** Strips a leading label word (e.g. "by "/"from ") off a "/"-delimited segment, returning what's left. */
    private static String afterLabel(String segment) {
        String trimmed = segment.trim();
        int spaceIndex = trimmed.indexOf(' ');
        return spaceIndex == -1 ? "" : trimmed.substring(spaceIndex + 1).trim();
    }

    /** Stores the given task in {@code history} and prints the "added" confirmation. */
    private static void addTask(Task task) {
        if (historyCount < MAX_HISTORY) {
            history[historyCount] = task;
            historyCount++;
        }
        printIndented("Got it. I've added this task:\n  " + task + "\nNow you have " + historyCount + " tasks in the list.");
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
            if (input.startsWith("todo")) {
                try {
                    String description = input.substring("todo".length()).trim();
                    addTask(new Todo(description));
                } catch (IllegalArgumentException e) {
                    printIndented("OOPS!!! " + e.getMessage());
                }
                System.out.println(DIVIDER);
                continue;
            }
            if (input.startsWith("deadline")) {
                try {
                    String[] parts = input.substring("deadline".length()).split("/", 2);
                    addTask(new Deadline(parts[0].trim(), afterLabel(parts[1])));
                } catch (ArrayIndexOutOfBoundsException e) {
                    printIndented("OOPS!!! A deadline needs a description and a /by date/time.");
                } catch (IllegalArgumentException e) {
                    printIndented("OOPS!!! " + e.getMessage());
                }
                System.out.println(DIVIDER);
                continue;
            }
            if (input.startsWith("event")) {
                try {
                    String[] parts = input.substring("event".length()).split("/", 3);
                    addTask(new Event(parts[0].trim(), afterLabel(parts[1]), afterLabel(parts[2])));
                } catch (ArrayIndexOutOfBoundsException e) {
                    printIndented("OOPS!!! An event needs a description, a /from and a /to date/time.");
                } catch (IllegalArgumentException e) {
                    printIndented("OOPS!!! " + e.getMessage());
                }
                System.out.println(DIVIDER);
                continue;
            }
            try {
                addTask(new Task(input));
            } catch (IllegalArgumentException e) {
                printIndented("OOPS!!! " + e.getMessage());
            }
            System.out.println(DIVIDER);
        }
        scanner.close();
    }
}
