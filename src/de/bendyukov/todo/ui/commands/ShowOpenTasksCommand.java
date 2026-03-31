package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.TaskTreeUtility;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;


/**
 * Command to show all open tasks.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class ShowOpenTasksCommand extends Command {
    private static final String COMMAND_NAME = "todo";

    /**
     * Creates show open tasks command.
     *
     * @param commandHandler command handler
     * @param app            todo app
     */
    public ShowOpenTasksCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes show open tasks command.
     *
     * @param args arguments (unused)
     * @throws InvalidCommandArgumentException if extra arguments given
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        requireNoAdditionalArguments(args);

        String output = TaskTreeUtility.buildSortedTree(app.getOpenTasks());

        if (!output.isEmpty()) {
            System.out.println(output);
        }
    }
}
