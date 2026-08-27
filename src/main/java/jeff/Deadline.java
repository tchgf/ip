package jeff;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A task that needs to be done by a specific date,
 * e.g. "submit report by 2019-10-15".
 */
public class Deadline extends Task {
    private static final DateTimeFormatter PRINT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    protected LocalDate by;

    public Deadline(String description, String by) {
        super(description);
        this.by = parseDate(by);
    }

    /**
     * Parses a "yyyy-mm-dd" date string, wrapping a blank or wrongly
     * formatted value as an {@link IllegalArgumentException} so callers can
     * handle it the same way as any other invalid task field.
     */
    private static LocalDate parseDate(String date) {
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("A deadline needs a /by date.");
        }
        try {
            return LocalDate.parse(date.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Please give the /by date in yyyy-mm-dd format, e.g. 2019-12-02.");
        }
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(PRINT_FORMAT) + ")";
    }

    @Override
    public String toSaveFormat() {
        return "D | " + super.toSaveFormat() + " | " + by;
    }
}
