package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.logic.TodoAppException;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

/**
 * Command to create new task list.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class CreateListCommand extends Command {
    private static final String COMMAND_NAME = "add-list";
    private static final String OUTPUT = "added %s";

    /**
     * Creates create list command.
     *
     * @param commandHandler command handler
     * @param app            todo app
     */
    public CreateListCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes create list command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        String name = args.getString();
        requireNoAdditionalArguments(args);

        try {
            app.createList(name);
            System.out.println(OUTPUT.formatted(name));
        } catch (TodoAppException exception) {
            throw new InvalidCommandArgumentException(exception.getMessage());
        }
    }
}

