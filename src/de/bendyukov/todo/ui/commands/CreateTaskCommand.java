package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.Task;
import de.bendyukov.todo.logic.TaskPriority;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

import java.time.LocalDate;

/**
 * Command to create new task.
 *
 * @author udkcf
 * @version 1.0
 */
public class CreateTaskCommand extends Command {
    private static final String COMMAND_NAME = "add";
    private static final String OUTPUT = "added %d: %s";
    private static final String ERROR_INVALID_PRIORITY_OR_DATE = "Invalid priority or date";
    private static final String ERROR_INVALID_DATE = "Invalid date";

    /**
     * Creates create task command.
     *
     * @param commandHandler command handler
     * @param app todo app
     */
    public CreateTaskCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes create task command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        String name = args.getString();
        String priorityOrDateString = args.getOptionalString();
        String dateString = args.getOptionalString();
        requireNoAdditionalArguments(args);

        TaskPriority priority = TaskPriority.findByCode(priorityOrDateString);

        LocalDate deadline;
        if (priority != null) {
            deadline = Task.parseDate(dateString);
            if (dateString != null && deadline == null) {
                throw new InvalidCommandArgumentException(ERROR_INVALID_DATE);
            }
        } else {
            deadline = Task.parseDate(priorityOrDateString);
            if (priorityOrDateString != null && deadline == null) {
                throw new InvalidCommandArgumentException(ERROR_INVALID_PRIORITY_OR_DATE);
            }
            if (dateString != null) {
                throw new InvalidCommandArgumentException(ERROR_TOO_MANY_ARGUMENTS);
            }
        }

        Task newTask = app.createTask(name, priority, deadline);

        System.out.println(OUTPUT.formatted(newTask.getId(), newTask.getName()));
    }
}
