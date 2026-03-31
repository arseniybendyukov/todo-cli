package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.Task;
import de.bendyukov.todo.logic.TaskTreeUtility;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

import java.util.List;

/**
 * Command to show task and children.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class ShowTaskCommand extends Command {
    private static final String COMMAND_NAME = "show";

    /**
     * Creates show task command.
     *
     * @param commandHandler command handler
     * @param app            todo app
     */
    public ShowTaskCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes show task command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        Task task = args.getActiveTaskById();
        requireNoAdditionalArguments(args);

        List<Task> tasks = app.getChildren(task);
        tasks.add(task);

        System.out.println(TaskTreeUtility.buildSortedTree(tasks));
    }
}
