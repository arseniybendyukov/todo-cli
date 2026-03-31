package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.Task;
import de.bendyukov.todo.logic.TaskPriority;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

/**
 * Command to change task priority.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class ChangePriorityCommand extends Command {
    private static final String COMMAND_NAME = "change-priority";
    private static final String NO_PRIORITY_PLACEHOLDER = "NONE";
    private static final String OUTPUT = "changed %s to %s";

    /**
     * Creates change priority command.
     *
     * @param commandHandler command handler
     * @param app            todo app
     */
    public ChangePriorityCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes change priority command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        Task task = args.getActiveTaskById();
        TaskPriority priority = args.getPriority();
        requireNoAdditionalArguments(args);

        app.changePriority(task, priority);
        System.out.println(OUTPUT.formatted(
                task.getName(),
                task.getPriority() != null ? task.getPriority().getCode() : NO_PRIORITY_PLACEHOLDER
        ));
    }
}
