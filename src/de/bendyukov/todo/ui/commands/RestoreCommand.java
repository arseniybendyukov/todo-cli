package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.Task;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.logic.TodoAppException;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

/**
 * Command to restore deleted task.
 *
 * @author udkcf
 * @version 1.0
 */
public class RestoreCommand extends Command {
    private static final String COMMAND_NAME = "restore";
    private static final String OUTPUT = "restored %s and %d subtasks";

    /**
     * Creates restore command.
     *
     * @param commandHandler command handler
     * @param app todo app
     */
    public RestoreCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes restore command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        Task task = args.getTaskById();
        requireNoAdditionalArguments(args);

        try {
            int count = app.restoreTask(task);
            System.out.println(OUTPUT.formatted(task.getName(), count));
        } catch (TodoAppException exception) {
            throw new InvalidCommandArgumentException(exception.getMessage());
        }
    }
}
