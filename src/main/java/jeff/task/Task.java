package jeff.task;

/**
 * A single to-do item with a description and a done/not-done status.
 * {@link Todo}, {@link Deadline}, and {@link Event} extend this with their
 * own extra fields (e.g. dates) and formatting.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new, not-done task with the given description.
     *
     * @param description what the task is; must not be null or blank.
     * @throws IllegalArgumentException if description is null or blank.
     */
    public Task(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("The description of a task cannot be empty.");
        }
        this.description = description;
        this.isDone = false;
    }

    /** Returns "X" if this task is done, or a blank space otherwise. */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /** Returns this task's description. */
    public String getDescription() {
        return description;
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void unmarkAsDone() {
        isDone = false;
    }

    /** Returns this task's status icon and description, e.g. "[X] read book". */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns this task's data encoded as a single line for {@link jeff.storage.Storage} to
     * write to disk. Subclasses prepend their own type letter and append any
     * extra fields (e.g. dates), separated by {@code " | "}.
     */
    public String toSaveFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }
}
