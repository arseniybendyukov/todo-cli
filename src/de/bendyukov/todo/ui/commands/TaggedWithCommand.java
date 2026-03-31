package de.bendyukov.todo.ui.commands;

import de.bendyukov.todo.logic.Tag;
import de.bendyukov.todo.logic.Task;
import de.bendyukov.todo.logic.TaskTreeUtility;
import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.logic.TodoAppException;
import de.bendyukov.todo.ui.ArgumentSupplier;
import de.bendyukov.todo.ui.Command;
import de.bendyukov.todo.ui.CommandHandler;
import de.bendyukov.todo.ui.InvalidCommandArgumentException;

import java.util.Set;

/**
 * Command to show tasks with tag.
 *
 * @author udkcf
 * @version 1.0
 */
public class TaggedWithCommand extends Command {
    private static final String COMMAND_NAME = "tagged-with";
    private static final String NEGATIVE_OUTPUT = "No tasks tagged with this tag found";

    /**
     * Creates tagged with command.
     *
     * @param commandHandler command handler
     * @param app todo app
     */
    public TaggedWithCommand(CommandHandler commandHandler, TodoApp app) {
        super(commandHandler, app, COMMAND_NAME);
    }

    /**
     * Executes tagged with command.
     *
     * @param args arguments
     * @throws InvalidCommandArgumentException if invalid arguments
     */
    @Override
    public void execute(ArgumentSupplier args) throws InvalidCommandArgumentException {
        String tagName = args.getString();
        requireNoAdditionalArguments(args);

        try {
            Tag tag = new Tag(tagName);

            Set<Task> tasks = app.getTaggedTasks(tag);

            if (tasks.isEmpty()) {
                System.out.println(NEGATIVE_OUTPUT);
            } else {
                String output = TaskTreeUtility.buildSortedTree(tasks);
                if (!output.isEmpty()) {
                    System.out.println(output);
                }
            }
        } catch (TodoAppException exception) {
            throw new InvalidCommandArgumentException(exception.getMessage());
        }
    }
}
