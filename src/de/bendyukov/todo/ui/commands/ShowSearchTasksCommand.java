package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.TaskTreeUtility;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

/**
 * Command to search tasks by name.
 *
 * @author udkcf
 * @version 1.0
 */
public class ShowSearchTasksCommand extends Command {
    private static final String COMMAND_NAME = "find";

    /**
     * Creates show search tasks command.
     *
     * @param commandHandler command handler
     * @param app todo app
     */
    public ShowSearchTasksCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes show search tasks command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        String query = args.getString();
        requireNoAdditionalArguments(args);

        String output = TaskTreeUtility.buildSortedTree(app.searchTasks(query));

        if (!output.isEmpty()) {
            System.out.println(output);
        }
    }
}
