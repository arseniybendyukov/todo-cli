package de.bendyukov.todo.logic;

/**
 * Priority levels for tasks.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public enum TaskPriority {
    /**
     * High priority.
     */
    HIGH("HI", 1),
    /**
     * Medium priority.
     */
    MEDIUM("MD", 2),
    /**
     * Low priority.
     */
    LOW("LO", 3);

    private final String code;
    private final int priority;

    TaskPriority(String code, int priority) {
        this.code = code;
        this.priority = priority;
    }

    /**
     * Finds priority by its code.
     *
     * @param code priority code to search for
     * @return matching priority or null if not found
     */
    public static TaskPriority findByCode(String code) {
        if (code == null) {
            return null;
        }

        for (TaskPriority item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Gets the priority code.
     *
     * @return priority code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Gets the priority value.
     *
     * @return priority value
     */
    public int getPriority() {
        return priority;
    }
}
