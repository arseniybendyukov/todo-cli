package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.TaskList;
import de.bendyukov.todo.logic.TaskTreeUtility;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

/**
 * Command to show tasks in list.
 *
 * @author udkcf
 * @version 1.0
 */
public class ShowListTasksCommand extends Command {
    private static final String COMMAND_NAME = "list";

    /**
     * Creates show list tasks command.
     *
     * @param commandHandler command handler
     * @param app todo app
     */
    public ShowListTasksCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes show list tasks command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        TaskList list = args.getListByName();
        requireNoAdditionalArguments(args);

        String output = TaskTreeUtility.buildSortedTree(app.getListTasks(list));

        if (!output.isEmpty()) {
            System.out.println(output);
        }
    }
}
