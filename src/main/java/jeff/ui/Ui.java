package jeff.ui;

import jeff.Task;

import java.util.List;
import java.util.Scanner;

/**
 * Handles all interaction with the user: printing Jeff's messages and
 * reading the user's typed input.
 */
public class Ui {
    private static final String INDENT = "    ";
    private static final String DIVIDER = INDENT + "____________________________________________________________";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Prints a (possibly multi-line) machine message with each line indented,
     * so it is visually distinct from the user's own typed input.
     */
    public void showMessage(String message) {
        for (String line : message.split("\n")) {
            System.out.println(INDENT + line);
        }
    }

    /** Prints the horizontal divider line used to separate turns of the conversation. */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /** Prints the startup banner and greeting. */
    public void showWelcome() {
        String banner = "     _  _____  _____  _____ \n"
                + "    | || ____||  ___||  ___|\n"
                + "    | || |__  | |_   | |_   \n"
                + "| |_| ||  __| |  _|  |  _|  \n"
                + " \\___/ |_____||_|    |_|    ";
        showLine();
        showMessage(banner);
        showMessage("Hello! I'm Jeff.");
        showMessage("What can I do for you?");
        showLine();
    }

    /** Prints the farewell message. */
    public void showBye() {
        showMessage("Bye. Hope to see you again soon!");
    }

    /** Prints an error message, prefixed the same way for every kind of failure. */
    public void showError(String message) {
        showMessage("OOPS!!! " + message);
    }

    /** Prints every given task as a numbered list, in order. */
    public void showTaskList(List<Task> tasks) {
        StringBuilder message = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            message.append("\n").append(i + 1).append(".").append(tasks.get(i));
        }
        showMessage(message.toString());
    }

    /** Prints confirmation that a task was added, e.g. after a "todo"/"deadline"/"event" command. */
    public void showTaskAdded(Task task, int taskCount) {
        showMessage("Got it. I've added this task:\n  " + task + "\nNow you have " + taskCount + " tasks in the list.");
    }

    /** Prints confirmation that a task was removed, e.g. after a "delete" command. */
    public void showTaskRemoved(Task task, int taskCount) {
        showMessage("Noted. I've removed this task:\n  " + task + "\nNow you have " + taskCount + " tasks in the list.");
    }

    /** Prints confirmation that a task was marked done. */
    public void showTaskMarked(Task task) {
        showMessage("Nice! I've marked this task as done:\n  " + task);
    }

    /** Prints confirmation that a task was marked not done. */
    public void showTaskUnmarked(Task task) {
        showMessage("OK, I've marked this task as not done yet:\n  " + task);
    }

    /** Reads one line of user input, or {@code null} if there is no more input (e.g. end of stream). */
    public String readCommand() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    /** Releases the underlying input resource; call once when Jeff is shutting down. */
    public void close() {
        scanner.close();
    }
}
