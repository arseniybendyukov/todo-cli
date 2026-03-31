package de.bendyukov.todo;

import de.bendyukov.todo.logic.TodoApp;
import de.bendyukov.todo.ui.CommandHandler;

/**
 * Entry point for the todo application.
 *
 * @author udkcf
 * @version 1.0
 */
public final class Main {
    private Main() {
    }

    /**
     * Main method to start the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        TodoApp app = new TodoApp();
        CommandHandler handler = new CommandHandler(app);
        handler.handleInput();
    }
}
