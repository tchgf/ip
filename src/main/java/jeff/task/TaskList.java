package jeff.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps the in-memory list of tasks, providing the operations needed to
 * add, remove, and look up tasks in it.
 */
public class TaskList {
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /** Creates a task list pre-populated with the given tasks, e.g. loaded from {@link jeff.storage.Storage}. */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds the given task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Removes and returns the task at the given zero-based index. */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** Returns the task at the given zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Returns how many tasks are in the list. */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying tasks, e.g. for {@link jeff.storage.Storage} to persist
     * or {@link jeff.ui.Ui} to display.
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /** Returns the tasks whose description contains the given keyword, in list order. */
    public List<Task> find(String keyword) {
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }
}
