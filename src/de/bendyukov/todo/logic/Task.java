package de.bendyukov.todo.logic;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Objects;

/**
 * Represents a task with priority, deadline, and parent-child relationships.
 *
 * @author udkcf
 * @version 1.0
 */
public class Task extends TaggableEntity {
    private static final String ERROR_CYCLE = "Cannot assign parent because of a cycle";
    private static final String ERROR_SELF_PARENTING = "Self parenting is not allowed";
    private static final String ERROR_ALREADY_PARENT = "The task is already its parent";
    private static final String DATE_FORMAT = "uuuu-MM-dd";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT).withResolverStyle(ResolverStyle.STRICT);

    private final long id;
    private final String name;
    private boolean isOpen = true;
    private boolean isDeleted = false;
    private TaskPriority priority;
    private LocalDate deadline;
    private Task parent;
    private Instant addedAt = Instant.now();

    /**
     * Creates a task.
     *
     * @param id unique task ID
     * @param name task name
     * @param priority task priority
     * @param deadline task deadline
     */
    public Task(long id, String name, TaskPriority priority, LocalDate deadline) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.deadline = deadline;
    }

    /**
     * Assigns a parent task to this task.
     *
     * @param parentCandidate parent task or null to remove parent
     * @throws TodoAppException if assignment creates cycle or self-parenting
     */
    public void assignParent(Task parentCandidate) throws TodoAppException {
        if (parentCandidate == null) {
            this.parent = null;
            return;
        }

        if (parentCandidate.equals(parent)) {
            throw new TodoAppException(ERROR_ALREADY_PARENT);
        }

        if (parentCandidate.isChild(this)) {
            throw new TodoAppException(ERROR_CYCLE);
        }

        if (parentCandidate.equals(this)) {
            throw new TodoAppException(ERROR_SELF_PARENTING);
        }

        this.parent = parentCandidate;
    }

    /**
     * Checks if this task is child of given parent.
     *
     * @param parentTask parent task to check
     * @return true if this is child of parentTask
     */
    public boolean isChild(Task parentTask) {
        if (parent == null && parentTask == null) {
            return true;
        }

        if (parent == null || this.equals(parentTask)) {
            return false;
        } else if (parent.equals(parentTask)) {
            return true;
        } else {
            return parent.isChild(parentTask);
        }
    }

    /**
     * Checks if this task is direct child of given parent.
     *
     * @param parentTask parent task to check
     * @return true if this is direct child
     */
    public boolean isDirectChild(Task parentTask) {
        if (parent == null || parentTask.equals(this)) {
            return false;
        }
        return parent.equals(parentTask);
    }

    /**
     * Gets task ID.
     *
     * @return task ID
     */
    public long getId() {
        return id;
    }

    /**
     * Gets task priority.
     *
     * @return priority
     */
    public TaskPriority getPriority() {
        return priority;
    }

    /**
     * Gets task name.
     *
     * @return task name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets open status.
     *
     * @return true if task is open
     */
    public boolean getIsOpen() {
        return isOpen;
    }

    /**
     * Gets deleted status.
     *
     * @return true if task is deleted
     */
    public boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * Gets parent task.
     *
     * @return parent task or null
     */
    public Task getParent() {
        return parent;
    }

    /**
     * Sets open status.
     *
     * @param isOpen new open status
     */
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * Sets deleted status.
     *
     * @param isDeleted new deleted status
     */
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * Sets task deadline.
     *
     * @param deadline new deadline
     */
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    /**
     * Sets task priority.
     *
     * @param priority new priority
     */
    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    /**
     * Gets when task was added.
     *
     * @return added timestamp
     */
    public Instant getAddedAt() {
        return addedAt;
    }

    /**
     * Sets added time to current time.
     */
    public void setTimeNow() {
        this.addedAt = Instant.now();
    }

    /**
     * Checks if this task is duplicate of another.
     *
     * @param otherTask task to compare
     * @return true if tasks are duplicates
     */
    public boolean isDuplicate(Task otherTask) {
        if (id == otherTask.getId() || !name.equals(otherTask.getName())) {
            return false;
        }

        if (deadline != null && otherTask.getDeadline() != null) {
            return deadline.equals(otherTask.getDeadline());
        }

        return true;
    }

    /**
     * Checks if name contains substring.
     *
     * @param substring substring to search for
     * @return true if name contains substring
     */
    public boolean containsSubstring(String substring) {
        return name.contains(substring);
    }

    /**
     * Gets task deadline.
     *
     * @return deadline
     */
    public LocalDate getDeadline() {
        return deadline;
    }

    /**
     * Parses date string to LocalDate.
     * @param dateString date string
     * @return parsed date or null if invalid
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        try {
            return LocalDate.parse(dateString, FORMATTER);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    /**
     * Formats LocalDate to string.
     * @param localDate date to format
     * @return formatted date string or null
     */
    public static String formatDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return localDate.format(FORMATTER);
    }

    /**
     * Checks equality based on task ID.
     *
     * @param object object to compare
     * @return true if same ID
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Task)) {
            return false;
        }

        Task other = (Task) object;

        return id == other.getId();
    }

    /**
     * Returns hash code based on ID.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
