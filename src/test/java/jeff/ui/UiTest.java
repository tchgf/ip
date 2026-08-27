package jeff.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jeff.task.Task;
import jeff.task.Todo;

public class UiTest {
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void redirectOut() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private String output() {
        return outContent.toString(StandardCharsets.UTF_8);
    }

    @Test
    public void showMessage_multilineMessage_indentsEachLine() {
        new Ui().showMessage("line one\nline two");
        String eol = System.lineSeparator();
        assertEquals("    line one" + eol + "    line two" + eol, output());
    }

    @Test
    public void showLine_printsIndentedDividerOfUnderscores() {
        new Ui().showLine();
        String printed = output();
        assertTrue(printed.startsWith("    "));
        assertTrue(printed.trim().chars().allMatch(c -> c == '_'));
    }

    @Test
    public void showWelcome_printsGreeting() {
        new Ui().showWelcome();
        assertTrue(output().contains("Hello! I'm Jeff."));
        assertTrue(output().contains("What can I do for you?"));
    }

    @Test
    public void showBye_printsFarewellMessage() {
        new Ui().showBye();
        assertEquals("    Bye. Hope to see you again soon!" + System.lineSeparator(), output());
    }

    @Test
    public void showError_prefixesMessageWithOops() {
        new Ui().showError("something went wrong.");
        assertEquals("    OOPS!!! something went wrong." + System.lineSeparator(), output());
    }

    @Test
    public void showTaskList_emptyList_printsHeaderOnly() {
        new Ui().showTaskList(List.of());
        assertEquals("    Here are the tasks in your list:" + System.lineSeparator(), output());
    }

    @Test
    public void showTaskList_numbersTasksInOrder() {
        Task first = new Todo("read book");
        Task second = new Todo("write essay");
        new Ui().showTaskList(List.of(first, second));

        String printed = output();
        assertTrue(printed.contains("1.[T][ ] read book"));
        assertTrue(printed.contains("2.[T][ ] write essay"));
    }

    @Test
    public void showTaskAdded_includesTaskAndCount() {
        new Ui().showTaskAdded(new Todo("read book"), 2);
        String printed = output();
        assertTrue(printed.contains("Got it. I've added this task:"));
        assertTrue(printed.contains("[T][ ] read book"));
        assertTrue(printed.contains("Now you have 2 tasks in the list."));
    }

    @Test
    public void showTaskRemoved_includesTaskAndCount() {
        new Ui().showTaskRemoved(new Todo("read book"), 1);
        String printed = output();
        assertTrue(printed.contains("Noted. I've removed this task:"));
        assertTrue(printed.contains("Now you have 1 tasks in the list."));
    }

    @Test
    public void showTaskMarked_includesTask() {
        Task task = new Todo("read book");
        task.markAsDone();
        new Ui().showTaskMarked(task);
        assertTrue(output().contains("[T][X] read book"));
    }

    @Test
    public void showTaskUnmarked_includesTask() {
        new Ui().showTaskUnmarked(new Todo("read book"));
        assertTrue(output().contains("[T][ ] read book"));
    }

    @Test
    public void readCommand_lineAvailable_returnsIt() {
        System.setIn(new ByteArrayInputStream("todo read book\n".getBytes(StandardCharsets.UTF_8)));
        Ui ui = new Ui();

        assertEquals("todo read book", ui.readCommand());
    }

    @Test
    public void readCommand_noMoreInput_returnsNull() {
        System.setIn(new ByteArrayInputStream(new byte[0]));
        Ui ui = new Ui();

        assertNull(ui.readCommand());
    }
}
