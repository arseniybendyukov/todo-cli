package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.Task;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

import java.time.LocalDate;

/**
 * Command to change task deadline.
 *
 * @author udkcf
 * @version 1.0
 */
public class ChangeDeadlineCommand extends Command {
    private static final String COMMAND_NAME = "change-date";
    private static final String OUTPUT = "changed %s to %s";

    /**
     * Creates change deadline command.
     *
     * @param commandHandler command handler
     * @param app todo app
     */
    public ChangeDeadlineCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes change deadline command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        Task task = args.getActiveTaskById();
        LocalDate deadline = args.getDate();
        requireNoAdditionalArguments(args);

        task.setDeadline(deadline);
        System.out.println(OUTPUT.formatted(task.getName(), Task.formatDate(task.getDeadline())));
    }
}
