package de.bendyukov.todo.logic;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a named list containing tasks.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class TaskList extends TaggableEntity {
    private static final String NAME_REGEX = "^[a-zA-Z]+$"; // Name contains 1 or more letters and nothing else
    private static final String ERROR_INVALID_NAME = "Only letters A-Z and a-z are allowed";

    private final String name;
    private final Set<Task> tasks = new LinkedHashSet<>();


    /**
     * Creates a task list with the given name.
     *
     * @param name list name (letters only)
     * @throws TodoAppException if name contains invalid characters
     */
    public TaskList(String name) throws TodoAppException {
        if (!name.matches(NAME_REGEX)) {
            throw new TodoAppException(ERROR_INVALID_NAME);
        }

        this.name = name;
    }

    /**
     * Gets the name of this list.
     *
     * @return name of the list
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a task to this list.
     *
     * @param task task to add
     */
    public void addTask(Task task) {
        if (task != null) {
            tasks.add(task);
        }
    }

    /**
     * Gets all tasks in this list.
     *
     * @return unmodifiable set of tasks
     */
    public Set<Task> getTasks() {
        return Collections.unmodifiableSet(tasks);
    }

    /**
     * Returns weather the task is in this list.
     *
     * @param task task to check
     * @return true if the task is in the list
     */
    public boolean hasTask(Task task) {
        return tasks.contains(task);
    }
}
