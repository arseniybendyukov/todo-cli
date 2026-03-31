package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.TaskTreeUtility;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

import java.time.LocalDate;

/**
 * Command to show upcoming tasks.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class UpcomingCommand extends Command {
    private static final String COMMAND_NAME = "upcoming";
    private static final int DAYS_DELTA = 6;

    /**
     * Creates upcoming command.
     *
     * @param commandHandler command handler
     * @param app            todo app
     */
    public UpcomingCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes upcoming command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        LocalDate date = args.getDate();
        requireNoAdditionalArguments(args);

        String output = TaskTreeUtility.buildSortedTree(app.getUpcomingTasks(date, DAYS_DELTA));

        if (!output.isEmpty()) {
            System.out.println(output);
        }
    }
}
