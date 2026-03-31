package de.bendyukov.todo.ui;

/**
 * Exception thrown when a command errors during execution.
 *
 * @author Programmieren-Team
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
