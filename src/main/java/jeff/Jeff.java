package jeff;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Jeff {
    private static final String INDENT = "    ";
    private static final String DIVIDER = INDENT + "____________________________________________________________";

    /** Store of every task the user has added so far, in order. */
    private static final List<Task> history = new ArrayList<>();

    /** The fixed set of commands Jeff understands, each tied to the exact word that triggers it. */
    private enum Command {
        BYE("bye"), LIST("list"), MARK("mark"), UNMARK("unmark"), DELETE("delete"),
        TODO("todo"), DEADLINE("deadline"), EVENT("event"), UNKNOWN("");

        private final String word;

        Command(String word) {
            this.word = word;
        }

        /** Looks up the command matching the given (case-sensitive) first word of user input. */
        static Command fromWord(String word) {
            for (Command command : values()) {
                if (command.word.equals(word)) {
                    return command;
                }
            }
            return UNKNOWN;
        }
    }

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
        for (int i = 0; i < history.size(); i++) {
            printIndented((i + 1) + "." + history.get(i));
        }
    }

    /**
     * Returns the {@code history} index for a "mark"/"unmark"/"delete" command's task-number argument.
     * Throws {@link NumberFormatException} if the number is missing/malformed, or
     * {@link IndexOutOfBoundsException} if it doesn't refer to an existing task.
     */
    private static int parseTaskIndex(String numberPart) {
        int index = Integer.parseInt(numberPart) - 1;
        if (index < 0 || index >= history.size()) {
            throw new IndexOutOfBoundsException("Task " + numberPart + " doesn't exist.");
        }
        return index;
    }

    /** Strips a leading label word (e.g. "by "/"from ") off a "/"-delimited segment, returning what's left. */
    private static String afterLabel(String segment) {
        String trimmed = segment.trim();
        int spaceIndex = trimmed.indexOf(' ');
        return spaceIndex == -1 ? "" : trimmed.substring(spaceIndex + 1).trim();
    }

    /** Stores the given task in {@code history} and prints the "added" confirmation. */
    private static void addTask(Task task) {
        history.add(task);
        printIndented("Got it. I've added this task:\n  " + task + "\nNow you have " + history.size() + " tasks in the list.");
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
            if (!scanner.hasNextLine()) {
                printIndented("Bye. Hope to see you again soon!");
                System.out.println(DIVIDER);
                break;
            }
            String input = scanner.nextLine();
            System.out.println(DIVIDER);

            // The first whitespace-separated word is the command; the rest are its arguments.
            String[] inputParts = input.split(" ", 2);
            Command command = Command.fromWord(inputParts[0]);
            String arguments = inputParts.length > 1 ? inputParts[1].trim() : "";

            switch (command) {
            case BYE:
                printIndented("Bye. Hope to see you again soon!");
                System.out.println(DIVIDER);
                scanner.close();
                return;
            case LIST:
                printHistory();
                break;
            case MARK:
                try {
                    Task task = history.get(parseTaskIndex(arguments));
                    task.markAsDone();
                    printIndented("Nice! I've marked this task as done:");
                    printIndented("  " + task);
                } catch (NumberFormatException e) {
                    printIndented("OOPS!!! Please provide a valid task number to mark.");
                } catch (IndexOutOfBoundsException e) {
                    printIndented("OOPS!!! " + e.getMessage());
                }
                break;
            case UNMARK:
                try {
                    Task task = history.get(parseTaskIndex(arguments));
                    task.unmarkAsDone();
                    printIndented("OK, I've marked this task as not done yet:");
                    printIndented("  " + task);
                } catch (NumberFormatException e) {
                    printIndented("OOPS!!! Please provide a valid task number to unmark.");
                } catch (IndexOutOfBoundsException e) {
                    printIndented("OOPS!!! " + e.getMessage());
                }
                break;
            case DELETE:
                try {
                    Task task = history.remove(parseTaskIndex(arguments));
                    printIndented("Noted. I've removed this task:\n  " + task
                            + "\nNow you have " + history.size() + " tasks in the list.");
                } catch (NumberFormatException e) {
                    printIndented("OOPS!!! Please provide a valid task number to delete.");
                } catch (IndexOutOfBoundsException e) {
                    printIndented("OOPS!!! " + e.getMessage());
                }
                break;
            case TODO:
                try {
                    addTask(new Todo(arguments));
                } catch (IllegalArgumentException e) {
                    printIndented("OOPS!!! " + e.getMessage());
                }
                break;
            case DEADLINE:
                try {
                    String[] parts = arguments.split("/", 2);
                    addTask(new Deadline(parts[0].trim(), afterLabel(parts[1])));
                } catch (ArrayIndexOutOfBoundsException e) {
                    printIndented("OOPS!!! A deadline needs a description and a /by date/time.");
                } catch (IllegalArgumentException e) {
                    printIndented("OOPS!!! " + e.getMessage());
                }
                break;
            case EVENT:
                try {
                    String[] parts = arguments.split("/", 3);
                    addTask(new Event(parts[0].trim(), afterLabel(parts[1]), afterLabel(parts[2])));
                } catch (ArrayIndexOutOfBoundsException e) {
                    printIndented("OOPS!!! An event needs a description, a /from and a /to date/time.");
                } catch (IllegalArgumentException e) {
                    printIndented("OOPS!!! " + e.getMessage());
                }
                break;
            default:
                printIndented("OOPS!!! I'm sorry, but I don't know what that command means.");
                break;
            }
            System.out.println(DIVIDER);
        }
        scanner.close();
    }
}
