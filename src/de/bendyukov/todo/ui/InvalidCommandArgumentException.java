package de.bendyukov.todo.ui;

/**
 * Exception thrown when a command errors during execution.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class InvalidCommandArgumentException extends Exception {
    private static final String PREFIX_ERROR = "ERROR: ";

    /**
     * Creates a new InvalidCommandArgumentException with prefixed message.
     *
     * @param message error message
     */
    public InvalidCommandArgumentException(String message) {
        super(PREFIX_ERROR + message);
    }
}
