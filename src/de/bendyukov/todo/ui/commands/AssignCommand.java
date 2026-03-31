package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.Task;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.logic.TodoAppException;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

/**
 * Command to assign task to parent or list.
 *
 * @author udkcf
 * @version 1.0
 */
public class AssignCommand extends Command {
    private static final String COMMAND_NAME = "assign";
    private static final String OUTPUT = "assigned %s to %s";

    /**
     * Creates assign command.
     *
     * @param commandHandler command handler
     * @param app todo app
     */
    public AssignCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes assign command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        Task task = args.getActiveTaskById();
        String taskIdOrListName = args.getString();
        requireNoAdditionalArguments(args);

        try  {
            long parentTaskId = Long.parseLong(taskIdOrListName);

            try {
                Task parentTask = app.getActiveTaskById(parentTaskId);
                app.assignParent(task.getId(), parentTask.getId());
                System.out.println(OUTPUT.formatted(task.getName(), parentTask.getName()));
            } catch (TodoAppException exception) {
                throw new InvalidCommandArgumentException(exception.getMessage());
            }
        } catch (NumberFormatException e) {
            String listName = taskIdOrListName;

            try {
                app.assignList(task.getId(), listName);
                System.out.println(OUTPUT.formatted(task.getName(), listName));
            } catch (TodoAppException exception) {
                throw new InvalidCommandArgumentException(exception.getMessage());
            }
        }
    }
}
