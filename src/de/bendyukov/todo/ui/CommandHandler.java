package de.bendyukov.todo.ui;

import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.ui.commands.AssignCommand;
import de.bendyukov.todo.ui.commands.BeforeCommand;
import de.bendyukov.todo.ui.commands.BetweenCommand;
import de.bendyukov.todo.ui.commands.ChangeDeadlineCommand;
import de.bendyukov.todo.ui.commands.ChangePriorityCommand;
import de.bendyukov.todo.ui.commands.CreateListCommand;
import de.bendyukov.todo.ui.commands.CreateTaskCommand;
import de.bendyukov.todo.ui.commands.DeleteCommand;
import de.bendyukov.todo.ui.commands.DuplicatesCommand;
import de.bendyukov.todo.ui.commands.QuitCommand;
import de.bendyukov.todo.ui.commands.RestoreCommand;
import de.bendyukov.todo.ui.commands.ShowListTasksCommand;
import de.bendyukov.todo.ui.commands.ShowOpenTasksCommand;
import de.bendyukov.todo.ui.commands.ShowSearchTasksCommand;
import de.bendyukov.todo.ui.commands.ShowTaskCommand;
import de.bendyukov.todo.ui.commands.TagCommand;
import de.bendyukov.todo.ui.commands.TaggedWithCommand;
import de.bendyukov.todo.ui.commands.ToggleCommand;
import de.bendyukov.todo.ui.commands.UpcomingCommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;


/**
 * Class implementing a command handler.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class CommandHandler {
    private static final String SEPARATOR = " ";
    private static final int COMMAND_NAME_INDEX = 0;
    private static final String ERROR_UNKNOWN_COMMAND = "ERROR: Unknown command";
    private static final int EMPTY_ARGS_LENGTH = 0;
    private static final int ARGS_START_INDEX = 1;
    private static final int MIN_ARGS_LENGTH = 2;

    private final TodoApp app;
    private final Map<String, Command> commands = new HashMap<>();
    private boolean quitting;

    /**
     * Creates command handler.
     *
     * @param app todo application
     */
    public CommandHandler(TodoApp app) {
        this.app = Objects.requireNonNull(app);
        initCommands();
        quitting = false;
    }

    private void initCommands() {
        addCommand(new AssignCommand(this, app));
        addCommand(new ChangeDeadlineCommand(this, app));
        addCommand(new ChangePriorityCommand(this, app));
        addCommand(new CreateListCommand(this, app));
        addCommand(new CreateTaskCommand(this, app));
        addCommand(new DeleteCommand(this, app));
        addCommand(new DuplicatesCommand(this, app));
        addCommand(new QuitCommand(this, app));
        addCommand(new RestoreCommand(this, app));
        addCommand(new BeforeCommand(this, app));
        addCommand(new BetweenCommand(this, app));
        addCommand(new ShowListTasksCommand(this, app));
        addCommand(new ShowOpenTasksCommand(this, app));
        addCommand(new ShowSearchTasksCommand(this, app));
        addCommand(new TaggedWithCommand(this, app));
        addCommand(new ShowTaskCommand(this, app));
        addCommand(new UpcomingCommand(this, app));
        addCommand(new TagCommand(this, app));
        addCommand(new ToggleCommand(this, app));
    }

    private void addCommand(Command command) {
        this.commands.put(command.getCommandName(), command);
    }

    /**
     * Signals application to quit.
     */
    public void quit() {
        this.quitting = true;
    }

    /**
     * Handles user input until quit.
     */
    public void handleInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (!quitting && scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.isBlank()) {
                    continue;
                }

                String[] inlineArgs = line.split(SEPARATOR);

                String commandName = inlineArgs[COMMAND_NAME_INDEX];

                if (!commands.containsKey(commandName)) {
                    System.out.println(ERROR_UNKNOWN_COMMAND);
                    continue;
                }

                String[] args;
                if (inlineArgs.length >= MIN_ARGS_LENGTH) {
                    args = Arrays.stream(inlineArgs, ARGS_START_INDEX, inlineArgs.length).toArray(String[]::new);
                } else {
                    args = new String[EMPTY_ARGS_LENGTH];
                }

                ArgumentSupplier supplier = new ArgumentSupplier(args, app);

                try {
                    commands.get(commandName).execute(supplier);
                } catch (InvalidCommandArgumentException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        }
    }
}
