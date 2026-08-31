package jeff;

import jeff.parser.Parser;
import jeff.storage.Storage;
import jeff.task.Deadline;
import jeff.task.Event;
import jeff.task.Task;
import jeff.task.TaskList;
import jeff.task.Todo;
import jeff.ui.Ui;

/**
 * Entry point for the Jeff chatbot. Wires together {@link Ui}, {@link Storage},
 * and {@link TaskList}, and drives the read-parse-execute loop that reads a
 * command from the user, asks {@link Parser} to make sense of it, and acts on it.
 */
public class Jeff {
    private static final String SAVE_FILE_PATH = "./data/jeff.txt";

    /** Every task the user has added so far, in order. */
    private static final TaskList taskList = new TaskList();

    /** Persists {@code taskList} to, and restores it from, {@link #SAVE_FILE_PATH}. */
    private static final Storage storage = new Storage(SAVE_FILE_PATH);

    /** Handles all printing to, and reading from, the user. */
    private static final Ui ui = new Ui();

    static {
        taskList.getTasks().addAll(storage.load());
    }

    /** Stores the given task in {@code taskList} and returns the "added" confirmation. */
    private static String addTask(Task task) {
        taskList.add(task);
        String response = ui.formatTaskAdded(task, taskList.size());
        storage.save(taskList.getTasks());
        return response;
    }

    /** Returns the greeting message shown when Jeff starts up, without the console's ASCII banner. */
    public static String getWelcomeMessage() {
        return ui.formatGreeting();
    }

    /**
     * Parses and executes a single line of user input, updating {@code taskList}
     * and {@code storage} as needed, and returns Jeff's response text. Shared by
     * both the console UI in {@link #main} and the JavaFX GUI.
     *
     * @param input a raw line of user input, e.g. {@code "todo read book"}.
     * @return the response text Jeff should show the user.
     */
    public static String getResponse(String input) {
        Parser.Command command = Parser.parseCommandType(input);
        String arguments = Parser.getArguments(input);

        switch (command) {
        case BYE:
            return ui.formatBye();
        case LIST:
            return ui.formatTaskList(taskList.getTasks());
        case MARK:
            try {
                Task task = taskList.get(Parser.parseTaskIndex(arguments, taskList.size()));
                task.markAsDone();
                String response = ui.formatTaskMarked(task);
                storage.save(taskList.getTasks());
                return response;
            } catch (NumberFormatException e) {
                return ui.formatError("Please provide a valid task number to mark.");
            } catch (IndexOutOfBoundsException e) {
                return ui.formatError(e.getMessage());
            }
        case UNMARK:
            try {
                Task task = taskList.get(Parser.parseTaskIndex(arguments, taskList.size()));
                task.unmarkAsDone();
                String response = ui.formatTaskUnmarked(task);
                storage.save(taskList.getTasks());
                return response;
            } catch (NumberFormatException e) {
                return ui.formatError("Please provide a valid task number to unmark.");
            } catch (IndexOutOfBoundsException e) {
                return ui.formatError(e.getMessage());
            }
        case DELETE:
            try {
                Task task = taskList.remove(Parser.parseTaskIndex(arguments, taskList.size()));
                String response = ui.formatTaskRemoved(task, taskList.size());
                storage.save(taskList.getTasks());
                return response;
            } catch (NumberFormatException e) {
                return ui.formatError("Please provide a valid task number to delete.");
            } catch (IndexOutOfBoundsException e) {
                return ui.formatError(e.getMessage());
            }
        case TODO:
            try {
                return addTask(new Todo(arguments));
            } catch (IllegalArgumentException e) {
                return ui.formatError(e.getMessage());
            }
        case DEADLINE:
            try {
                String[] parts = Parser.splitDeadlineArgs(arguments);
                return addTask(new Deadline(parts[0], parts[1]));
            } catch (ArrayIndexOutOfBoundsException e) {
                return ui.formatError("A deadline needs a description and a /by date/time.");
            } catch (IllegalArgumentException e) {
                return ui.formatError(e.getMessage());
            }
        case EVENT:
            try {
                String[] parts = Parser.splitEventArgs(arguments);
                return addTask(new Event(parts[0], parts[1], parts[2]));
            } catch (ArrayIndexOutOfBoundsException e) {
                return ui.formatError("An event needs a description, a /from and a /to date/time.");
            } catch (IllegalArgumentException e) {
                return ui.formatError(e.getMessage());
            }
        case FIND:
            if (arguments.isEmpty()) {
                return ui.formatError("Please provide a keyword to search for.");
            }
            return ui.formatMatchingTasks(taskList.find(arguments));
        default:
            return ui.formatError("I'm sorry, but I don't know what that command means.");
        }
    }

    /**
     * Greets the user, then repeatedly reads and executes commands until a
     * "bye" command or end of input is reached.
     *
     * @param args unused; Jeff takes no command-line arguments.
     */
    public static void main(String[] args) {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            if (input == null) {
                ui.showBye();
                ui.showLine();
                break;
            }
            ui.showLine();
            ui.showMessage(getResponse(input));
            ui.showLine();
            if (Parser.parseCommandType(input) == Parser.Command.BYE) {
                break;
            }
        }
        ui.close();
    }
}
