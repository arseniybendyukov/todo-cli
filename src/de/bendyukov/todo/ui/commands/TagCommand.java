package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.Task;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.logic.TodoAppException;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

/**
 * Command to tag task or list.
 *
 * @author udkcf
 * @version 1.0
 */
public class TagCommand extends Command {
    private static final String COMMAND_NAME = "tag";
    private static final String OUTPUT = "tagged %s with %s";

    /**
     * Creates tag command.
     *
     * @param commandHandler command handler
     * @param app todo app
     */
    public TagCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes tag command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        String taskIdOrListName = args.getString();
        String tagName = args.getString();
        requireNoAdditionalArguments(args);

        try  {
            long taskId = Long.parseLong(taskIdOrListName);

            try {
                Task task = app.tagTask(taskId, tagName);
                System.out.println(OUTPUT.formatted(task.getName(), tagName));
            } catch (TodoAppException exception) {
                throw new InvalidCommandArgumentException(exception.getMessage());
            }
        } catch (NumberFormatException e) {
            String listName = taskIdOrListName;

            try {
                app.tagList(listName, tagName);
                System.out.println(OUTPUT.formatted(listName, tagName));
            } catch (TodoAppException exception) {
                throw new InvalidCommandArgumentException(exception.getMessage());
            }
        }
    }
}
