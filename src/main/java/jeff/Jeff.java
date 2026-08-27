package jeff;

public class Jeff {
    private static final String SAVE_FILE_PATH = "./data/jeff.txt";

    /** Every task the user has added so far, in order. */
    private static final TaskList taskList = new TaskList();

    /** Persists {@code taskList} to, and restores it from, {@link #SAVE_FILE_PATH}. */
    private static final Storage storage = new Storage(SAVE_FILE_PATH);

    /** Handles all printing to, and reading from, the user. */
    private static final Ui ui = new Ui();

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
     * Returns the {@code taskList} index for a "mark"/"unmark"/"delete" command's task-number argument.
     * Throws {@link NumberFormatException} if the number is missing/malformed, or
     * {@link IndexOutOfBoundsException} if it doesn't refer to an existing task.
     */
    private static int parseTaskIndex(String numberPart) {
        int index = Integer.parseInt(numberPart) - 1;
        if (index < 0 || index >= taskList.size()) {
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

            // The first whitespace-separated word is the command; the rest are its arguments.
            String[] inputParts = input.split(" ", 2);
            Command command = Command.fromWord(inputParts[0]);
            String arguments = inputParts.length > 1 ? inputParts[1].trim() : "";

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
                    Task task = taskList.get(parseTaskIndex(arguments));
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
                    Task task = taskList.get(parseTaskIndex(arguments));
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
                    Task task = taskList.remove(parseTaskIndex(arguments));
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
                    String[] parts = arguments.split("/", 2);
                    addTask(new Deadline(parts[0].trim(), afterLabel(parts[1])));
                } catch (ArrayIndexOutOfBoundsException e) {
                    ui.showError("A deadline needs a description and a /by date/time.");
                } catch (IllegalArgumentException e) {
                    ui.showError(e.getMessage());
                }
                break;
            case EVENT:
                try {
                    String[] parts = arguments.split("/", 3);
                    addTask(new Event(parts[0].trim(), afterLabel(parts[1]), afterLabel(parts[2])));
                } catch (ArrayIndexOutOfBoundsException e) {
                    ui.showError("An event needs a description, a /from and a /to date/time.");
                } catch (IllegalArgumentException e) {
                    ui.showError(e.getMessage());
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
