package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;

/**
 * Command to quit the application.
 *
 * @author Programmieren-Team
 */
public class QuitCommand extends Command {
    private static final String COMMAND_NAME = "quit";

    /**
     * Creates quit command.
     *
     * @param commandHandler command handler
     * @param app todo app
     */
    public QuitCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes quit command.
     *
     * @param args arguments (unused)
     */
    @Override
    public void execute(ArgumentSupplier args) {
        commandHandler.quit();
    }
}
