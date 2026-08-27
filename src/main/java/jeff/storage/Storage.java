package jeff.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import jeff.task.Deadline;
import jeff.task.Event;
import jeff.task.Task;
import jeff.task.Todo;

/**
 * Reads tasks from, and writes tasks to, a fixed file on disk, so that the
 * task list persists between runs of the chatbot.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a Storage bound to the given save file path (which need not exist yet).
     *
     * @param filePath path (relative or absolute) of the file to load from and save to.
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Loads previously saved tasks from disk. Returns an empty list if the
     * save file does not exist yet (e.g. on first run). Any line that is
     * corrupted or unreadable is skipped, so a single bad line does not
     * prevent the rest of the file from loading.
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }
        try {
            for (String line : Files.readAllLines(filePath)) {
                Task task = parseLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("OOPS!!! Could not read saved tasks: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Parses a single save-file line (see {@link Task#toSaveFormat()} for the
     * format) into the matching task, or returns {@code null} if the line is
     * corrupted or its type letter is unrecognized.
     */
    private Task parseLine(String line) {
        String[] parts = line.split(" \\| ");
        try {
            boolean isDone = parts[1].equals("1");
            String description = parts[2];
            Task task;
            switch (parts[0]) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                task = new Deadline(description, parts[3]);
                break;
            case "E":
                task = new Event(description, parts[3], parts[4]);
                break;
            default:
                return null;
            }
            if (isDone) {
                task.markAsDone();
            }
            return task;
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Writes the given tasks to disk, one per line, creating the save
     * file's parent folder first if it does not already exist.
     */
    public void save(List<Task> tasks) {
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            StringBuilder content = new StringBuilder();
            for (Task task : tasks) {
                content.append(task.toSaveFormat()).append(System.lineSeparator());
            }
            Files.writeString(filePath, content.toString());
        } catch (IOException e) {
            System.out.println("OOPS!!! Could not save tasks: " + e.getMessage());
        }
    }
}
