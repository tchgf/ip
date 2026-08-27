package jeff;

import jeff.parser.Parser;
import jeff.storage.Storage;
import jeff.task.Deadline;
import jeff.task.Event;
import jeff.task.Task;
import jeff.task.TaskList;
import jeff.task.Todo;
import jeff.ui.Ui;

public class Jeff {
    private static final String SAVE_FILE_PATH = "./data/jeff.txt";

    /** Every task the user has added so far, in order. */
    private static final TaskList taskList = new TaskList();

    /** Persists {@code taskList} to, and restores it from, {@link #SAVE_FILE_PATH}. */
    private static final Storage storage = new Storage(SAVE_FILE_PATH);

    /** Handles all printing to, and reading from, the user. */
    private static final Ui ui = new Ui();

    /** Stores the given task in {@code taskList} and prints the "added" confirmation. */
    private static void addTask(Task task) {
        taskList.add(task);
        ui.showTaskAdded(task, taskList.size());
        storage.save(taskList.getTasks());
    }

    public static void main(String[] args) {
        taskList.getTasks().addAll(storage.load());
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            if (input == null) {
                ui.showBye();
                ui.showLine();
                break;
            }
            ui.showLine();

            Parser.Command command = Parser.parseCommandType(input);
            String arguments = Parser.getArguments(input);

            switch (command) {
            case BYE:
                ui.showBye();
                ui.showLine();
                ui.close();
                return;
            case LIST:
                ui.showTaskList(taskList.getTasks());
                break;
            case MARK:
                try {
                    Task task = taskList.get(Parser.parseTaskIndex(arguments, taskList.size()));
                    task.markAsDone();
                    ui.showTaskMarked(task);
                    storage.save(taskList.getTasks());
                } catch (NumberFormatException e) {
                    ui.showError("Please provide a valid task number to mark.");
                } catch (IndexOutOfBoundsException e) {
                    ui.showError(e.getMessage());
                }
                break;
            case UNMARK:
                try {
                    Task task = taskList.get(Parser.parseTaskIndex(arguments, taskList.size()));
                    task.unmarkAsDone();
                    ui.showTaskUnmarked(task);
                    storage.save(taskList.getTasks());
                } catch (NumberFormatException e) {
                    ui.showError("Please provide a valid task number to unmark.");
                } catch (IndexOutOfBoundsException e) {
                    ui.showError(e.getMessage());
                }
                break;
            case DELETE:
                try {
                    Task task = taskList.remove(Parser.parseTaskIndex(arguments, taskList.size()));
                    ui.showTaskRemoved(task, taskList.size());
                    storage.save(taskList.getTasks());
                } catch (NumberFormatException e) {
                    ui.showError("Please provide a valid task number to delete.");
                } catch (IndexOutOfBoundsException e) {
                    ui.showError(e.getMessage());
                }
                break;
            case TODO:
                try {
                    addTask(new Todo(arguments));
                } catch (IllegalArgumentException e) {
                    ui.showError(e.getMessage());
                }
                break;
            case DEADLINE:
                try {
                    String[] parts = Parser.splitDeadlineArgs(arguments);
                    addTask(new Deadline(parts[0], parts[1]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    ui.showError("A deadline needs a description and a /by date/time.");
                } catch (IllegalArgumentException e) {
                    ui.showError(e.getMessage());
                }
                break;
            case EVENT:
                try {
                    String[] parts = Parser.splitEventArgs(arguments);
                    addTask(new Event(parts[0], parts[1], parts[2]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    ui.showError("An event needs a description, a /from and a /to date/time.");
                } catch (IllegalArgumentException e) {
                    ui.showError(e.getMessage());
                }
                break;
            case FIND:
                if (arguments.isEmpty()) {
                    ui.showError("Please provide a keyword to search for.");
                } else {
                    ui.showMatchingTasks(taskList.find(arguments));
                }
                break;
            default:
                ui.showError("I'm sorry, but I don't know what that command means.");
                break;
            }
            ui.showLine();
        }
    }
}
