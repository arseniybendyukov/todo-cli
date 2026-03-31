package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

import java.util.SortedSet;
import java.util.StringJoiner;

/**
 * Command to find duplicate tasks.
 *
 * @author udkcf
 * @version 1.0
 */
public class DuplicatesCommand extends Command {
    private static final String COMMAND_NAME = "duplicates";
    private static final String OUTPUT = "Found %d duplicates: %s";
    private static final String SEPARATOR = ", ";

    /**
     * Creates duplicates command.
     *
     * @param commandHandler command handler
     * @param app todo app
     */
    public DuplicatesCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes duplicates command.
     *
     * @param args arguments (unused)
     * @throws InvalidCommandArgumentException if extra arguments given
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        requireNoAdditionalArguments(args);
        SortedSet<Long> ids = app.getDuplicateTasks();
        StringJoiner joiner = new StringJoiner(SEPARATOR);
        for (long id: ids) {
            joiner.add(String.valueOf(id));
        }
        System.out.println(OUTPUT.formatted(ids.size(), joiner));
    }
}
