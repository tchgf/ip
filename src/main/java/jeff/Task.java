package jeff;

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
}
