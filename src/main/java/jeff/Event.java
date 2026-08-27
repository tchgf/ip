package jeff;

/**
 * A task that starts at a specific date/time and ends at a specific date/time,
 * e.g. "team project meeting 2/10/2019 2-4pm".
 */
public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        if (from == null || from.isBlank()) {
            throw new IllegalArgumentException("An event needs a /from date/time.");
        }
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("An event needs a /to date/time.");
        }
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
