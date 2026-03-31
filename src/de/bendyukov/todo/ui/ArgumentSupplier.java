package de.bendyukov.todo.ui;


import de.bendyukov.todo.logic.Task;
import de.bendyukov.todo.logic.TaskList;
import de.bendyukov.todo.logic.TaskPriority;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.logic.TodoAppException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class supplying arguments to commands.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class ArgumentSupplier {
    private static final String ERROR_TOO_FEW_ARGUMENTS = "Too few arguments";
    private static final String ERROR_INVALID_ID = "Invalid id format";
    private static final String ERROR_INVALID_DATE = "Invalid date format";
    private static final String ERROR_INVALID_PRIORITY = "Invalid priority";

    private final List<String> remainingArgs;
    private final TodoApp app;

    /**
     * Creates a new ArgumentSupplier.
     *
     * @param args String arguments of the command
     * @param app  TodoApp to execute commands with
     */
    public ArgumentSupplier(String[] args, TodoApp app) {
        this.remainingArgs = new ArrayList<>(Arrays.asList(args));
        this.app = app;
    }

    private String getAndRemoveFirst() throws InvalidCommandArgumentException {
        if (this.remainingArgs.isEmpty()) {
            throw new InvalidCommandArgumentException(ERROR_TOO_FEW_ARGUMENTS);
        }
        return this.remainingArgs.removeFirst();
    }

    /**
     * Returns active (non-deleted) task specified by the next string argument.
     *
     * @return The found active task
     * @throws InvalidCommandArgumentException if no argument was supplied or no active task could be found
     */
    public Task getActiveTaskById() throws InvalidCommandArgumentException {
        return getTaskById(true);
    }

    /**
     * Returns task specified by the next string argument.
     *
     * @return The found task
     * @throws InvalidCommandArgumentException if no argument was supplied or no task could be found
     */
    public Task getTaskById() throws InvalidCommandArgumentException {
        return getTaskById(false);
    }

    private Task getTaskById(boolean onlyActive) throws InvalidCommandArgumentException {
        try {
            long taskId = Long.parseLong(this.getAndRemoveFirst());

            try {
                if (onlyActive) {
                    return app.getActiveTaskById(taskId);
                } else {
                    return app.getTaskById(taskId);
                }
            } catch (TodoAppException exception) {
                throw new InvalidCommandArgumentException(exception.getMessage());
            }
        } catch (NumberFormatException e) {
            throw new InvalidCommandArgumentException(ERROR_INVALID_ID);
        }
    }

    /**
     * Returns list specified by the next string argument.
     *
     * @return The found list
     * @throws InvalidCommandArgumentException if no argument was supplied or no list could be found
     */
    public TaskList getListByName() throws InvalidCommandArgumentException {
        String listName = this.getAndRemoveFirst();

        try {
            return app.getListByName(listName);
        } catch (TodoAppException exception) {
            throw new InvalidCommandArgumentException(exception.getMessage());
        }
    }

    /**
     * Parses and returns date specified by the next string argument.
     *
     * @return parsed date
     * @throws InvalidCommandArgumentException if invalid date format
     */
    public LocalDate getDate() throws InvalidCommandArgumentException {
        LocalDate date = Task.parseDate(this.getAndRemoveFirst());
        if (date == null) {
            throw new InvalidCommandArgumentException(ERROR_INVALID_DATE);
        }
        return date;
    }

    /**
     * Parses and returns priority specified by the next string argument.
     *
     * @return priority or null
     * @throws InvalidCommandArgumentException if invalid priority code
     */
    public TaskPriority getPriority() throws InvalidCommandArgumentException {
        String code = this.getOptionalString();
        TaskPriority priority = TaskPriority.findByCode(code);
        if (code != null && priority == null) {
            throw new InvalidCommandArgumentException(ERROR_INVALID_PRIORITY);
        }
        return priority;
    }

    /**
     * Returns the next string specified by the next string argument.
     *
     * @return the next string
     * @throws InvalidCommandArgumentException if no argument was supplied
     */
    public String getString() throws InvalidCommandArgumentException {
        return this.getAndRemoveFirst();
    }

    /**
     * Returns the next string specified by the next string argument
     * or null if no argument was supplied.
     *
     * @return string or null
     */
    public String getOptionalString() {
        return !this.remainingArgs.isEmpty() ? this.remainingArgs.removeFirst() : null;
    }

    /**
     * Returns whether more argument are present.
     *
     * @return true, if the supplier has remaining arguments, false otherwise
     */
    public boolean hasMoreArguments() {
        return !this.remainingArgs.isEmpty();
    }
}
