package de.bendyukov.todo.ui;

import de.bendyukov.todo.logic.TodoApp;

import java.util.Objects;

/**
 * Class implementing a single command.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public abstract class Command {
    /**
     * Error message shown when a command recieved more arguments than expected.
     */
    protected static final String ERROR_TOO_MANY_ARGUMENTS = "Too many arguments";

    private final String commandName;

    /**
     * The command handler instance.
     */
    protected final CommandHandler commandHandler;

    /**
     * The app instance.
     */
    protected final TodoApp app;

    /**
     * Creates a new command.
     * 
     * @param commandHandler Command handler to execute commands with
     * @param app Todo app to execute commands with
     * @param commandName Name of the command
     */
    protected Command(CommandHandler commandHandler, TodoApp app, String commandName) {
        this.commandHandler = Objects.requireNonNull(commandHandler);
        this.app = Objects.requireNonNull(app);
        this.commandName = Objects.requireNonNull(commandName);
    }

    /**
     * Returns the name of this command.
     * 
     * @return the name of this command
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Executes this command.
     *
     * @param args argument supplier
     * @throws InvalidCommandArgumentException if arguments invalid
     */
    public abstract void execute(ArgumentSupplier args) throws InvalidCommandArgumentException;

    /**
     * Ensures no additional arguments.
     *
     * @param args argument supplier
     * @throws InvalidCommandArgumentException if extra arguments are present
     */
    protected static void requireNoAdditionalArguments(ArgumentSupplier args) throws InvalidCommandArgumentException {
        if (args.hasMoreArguments()) {
            throw new InvalidCommandArgumentException(ERROR_TOO_MANY_ARGUMENTS);
        }
    }
}
