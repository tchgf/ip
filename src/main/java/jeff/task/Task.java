package jeff.task;

public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("The description of a task cannot be empty.");
        }
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /** Returns this task's description. */
    public String getDescription() {
        return description;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void unmarkAsDone() {
        isDone = false;
    }

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
