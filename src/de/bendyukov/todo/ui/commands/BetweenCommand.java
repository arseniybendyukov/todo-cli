package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.TaskTreeUtility;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

import java.time.LocalDate;

/**
 * Command to show tasks between dates.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class BetweenCommand extends Command {
    private static final String COMMAND_NAME = "between";

    /**
     * Creates between command.
     *
     * @param commandHandler command handler
     * @param app            todo app
     */
    public BetweenCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes between command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        LocalDate startDate = args.getDate();
        LocalDate endDate = args.getDate();
        requireNoAdditionalArguments(args);

        String output = TaskTreeUtility.buildSortedTree(app.getTasksBetweenDates(startDate, endDate));

        if (!output.isEmpty()) {
            System.out.println(output);
        }
    }
}
