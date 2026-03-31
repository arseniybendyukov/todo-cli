package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.TaskTreeUtility;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

import java.time.LocalDate;

/**
 * Command to show tasks before date.
 *
 * @author udkcf
 * @version 1.0
 */
public class BeforeCommand extends Command {
    private static final String COMMAND_NAME = "before";

    /**
     * Creates before command.
     *
     * @param commandHandler command handler
     * @param app todo app
     */
    public BeforeCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes before command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        LocalDate date = args.getDate();
        requireNoAdditionalArguments(args);

        String output = TaskTreeUtility.buildSortedTree(app.getBeforeTasks(date));

        if (!output.isEmpty()) {
            System.out.println(output);
        }
    }
}
