package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.Task;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.logic.TodoAppException;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

/**
 * Command to delete task and children.
 *
 * @author udkcf
 * @version 1.0
 */
public class DeleteCommand extends Command {
    private static final String COMMAND_NAME = "delete";
    private static final String OUTPUT = "deleted %s and %d subtasks";

    /**
     * Creates delete command.
     *
     * @param commandHandler command handler
     * @param app todo app
     */
    public DeleteCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes delete command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        Task task = args.getActiveTaskById();
        requireNoAdditionalArguments(args);

        try {
            int count = app.deleteTask(task);
            System.out.println(OUTPUT.formatted(task.getName(), count));
        } catch (TodoAppException exception) {
            throw new InvalidCommandArgumentException(exception.getMessage());
        }
    }
}
