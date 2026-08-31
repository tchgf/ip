package jeff.ui;

import java.util.List;
import java.util.Scanner;

import jeff.task.Task;

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
        showMessage(formatGreeting());
        showLine();
    }

    /** Builds the greeting shown when Jeff starts up, without the ASCII banner. */
    public String formatGreeting() {
        return "Hello! I'm Jeff.\nWhat can I do for you?";
    }

    /** Prints the farewell message. */
    public void showBye() {
        showMessage(formatBye());
    }

    /** Builds the farewell message. */
    public String formatBye() {
        return "Bye. Hope to see you again soon!";
    }

    /** Prints an error message, prefixed the same way for every kind of failure. */
    public void showError(String message) {
        showMessage(formatError(message));
    }

    /** Builds an error message, prefixed the same way for every kind of failure. */
    public String formatError(String message) {
        return "OOPS!!! " + message;
    }

    /** Prints every given task as a numbered list, in order. */
    public void showTaskList(List<Task> tasks) {
        showMessage(formatTaskList(tasks));
    }

    /** Builds the numbered list of every given task, in order. */
    public String formatTaskList(List<Task> tasks) {
        return formatNumberedTasks("Here are the tasks in your list:", tasks);
    }

    /** Prints every given task that matched a "find" search, numbered in order. */
    public void showMatchingTasks(List<Task> tasks) {
        showMessage(formatMatchingTasks(tasks));
    }

    /** Builds the numbered list of every given task that matched a "find" search, in order. */
    public String formatMatchingTasks(List<Task> tasks) {
        return formatNumberedTasks("Here are the matching tasks in your list:", tasks);
    }

    /** Builds the given header followed by each task, numbered from 1 in the given order. */
    private String formatNumberedTasks(String header, List<Task> tasks) {
        StringBuilder message = new StringBuilder(header);
        for (int i = 0; i < tasks.size(); i++) {
            message.append("\n").append(i + 1).append(".").append(tasks.get(i));
        }
        return message.toString();
    }

    /** Prints confirmation that a task was added, e.g. after a "todo"/"deadline"/"event" command. */
    public void showTaskAdded(Task task, int taskCount) {
        showMessage(formatTaskAdded(task, taskCount));
    }

    /** Builds confirmation that a task was added, e.g. after a "todo"/"deadline"/"event" command. */
    public String formatTaskAdded(Task task, int taskCount) {
        return "Got it. I've added this task:\n  " + task + "\nNow you have " + taskCount + " tasks in the list.";
    }

    /** Prints confirmation that a task was removed, e.g. after a "delete" command. */
    public void showTaskRemoved(Task task, int taskCount) {
        showMessage(formatTaskRemoved(task, taskCount));
    }

    /** Builds confirmation that a task was removed, e.g. after a "delete" command. */
    public String formatTaskRemoved(Task task, int taskCount) {
        return "Noted. I've removed this task:\n  " + task + "\nNow you have " + taskCount + " tasks in the list.";
    }

    /** Prints confirmation that a task was marked done. */
    public void showTaskMarked(Task task) {
        showMessage(formatTaskMarked(task));
    }

    /** Builds confirmation that a task was marked done. */
    public String formatTaskMarked(Task task) {
        return "Nice! I've marked this task as done:\n  " + task;
    }

    /** Prints confirmation that a task was marked not done. */
    public void showTaskUnmarked(Task task) {
        showMessage(formatTaskUnmarked(task));
    }

    /** Builds confirmation that a task was marked not done. */
    public String formatTaskUnmarked(Task task) {
        return "OK, I've marked this task as not done yet:\n  " + task;
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
