package de.bendyukov.todo.logic;

/**
 * Exception for todo app errors.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class TodoAppException extends Exception {
    /**
     * Creates exception with message.
     *
     * @param message error message
     */
    public TodoAppException(String message) {
        super(message);
    }
}
