package jeff.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TaskListTest {
    @Test
    public void constructor_noArgs_startsEmpty() {
        TaskList taskList = new TaskList();
        assertEquals(0, taskList.size());
    }

    @Test
    public void constructor_withTasks_copiesGivenList() {
        List<Task> initial = new ArrayList<>();
        initial.add(new Todo("read book"));
        TaskList taskList = new TaskList(initial);

        initial.add(new Todo("write essay"));

        assertEquals(1, taskList.size());
    }

    @Test
    public void add_increasesSizeAndStoresTaskAtEnd() {
        TaskList taskList = new TaskList();
        Task task = new Todo("read book");

        taskList.add(task);

        assertEquals(1, taskList.size());
        assertSame(task, taskList.get(0));
    }

    @Test
    public void remove_returnsRemovedTaskAndDecreasesSize() {
        TaskList taskList = new TaskList();
        Task first = new Todo("read book");
        Task second = new Todo("write essay");
        taskList.add(first);
        taskList.add(second);

        Task removed = taskList.remove(0);

        assertSame(first, removed);
        assertEquals(1, taskList.size());
        assertSame(second, taskList.get(0));
    }

    @Test
    public void getTasks_reflectsAddsAndRemovesInOrder() {
        TaskList taskList = new TaskList();
        Task first = new Todo("read book");
        Task second = new Todo("write essay");
        taskList.add(first);
        taskList.add(second);

        List<Task> tasks = taskList.getTasks();

        assertEquals(List.of(first, second), tasks);
    }
}
