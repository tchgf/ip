package jeff;

/**
 * A task with no date or time attached to it, e.g. "visit new theme park".
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
