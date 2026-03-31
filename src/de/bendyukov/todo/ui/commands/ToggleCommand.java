package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.Task;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.logic.TodoAppException;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

/**
 * Command to toggle task status.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class ToggleCommand extends Command {
    private static final String COMMAND_NAME = "toggle";
    private static final String OUTPUT = "toggled %s and %d subtasks";

    /**
     * Creates toggle command.
     *
     * @param commandHandler command handler
     * @param app            todo app
     */
    public ToggleCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes toggle command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        Task task = args.getActiveTaskById();
        requireNoAdditionalArguments(args);

        try {
            int count = app.toggle(task.getId());
            System.out.println(OUTPUT.formatted(task.getName(), count));
        } catch (TodoAppException exception) {
            throw new InvalidCommandArgumentException(exception.getMessage());
        }
    }
}
