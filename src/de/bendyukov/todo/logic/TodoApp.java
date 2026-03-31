package de.bendyukov.todo.logic;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Main application logic for todo management.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class TodoApp {
    private static final String ERROR_UNIQUE_LIST_NAME = "List names must be unique";
    private static final String ERROR_NO_SUCH_TASK = "There is no such task";
    private static final String ERROR_NO_SUCH_LIST = "There is no such list";
    private static final String ERROR_DELETED_TASK = "This action cannot be done, because this task was deleted";
    private static final String ERROR_ALREADY_DELETED = "Cannot delete an already deleted task";
    private static final String ERROR_RESTORE_EXISTING_TASK = "Cannot restore existing task";
    private static final String ERROR_ALREADY_IN_LIST = "This task is already in the list";
    private static final String ERROR_ALREADY_TAGGED = "This task already has this tag";
    private static final int INITIAL_ID = 1;
    private static final int INITIAL_TASKS_COUNT = 0;
    private final Map<Long, Task> tasks = new HashMap<>();
    private final Map<String, TaskList> lists = new HashMap<>();
    private long nextId = INITIAL_ID;

    /**
     * Creates a new task.
     *
     * @param name     task name
     * @param priority task priority
     * @param deadline task deadline
     * @return created task
     */
    public Task createTask(String name, TaskPriority priority, LocalDate deadline) {
        Task newTask = new Task(nextId, name, priority, deadline);
        tasks.put(nextId, newTask);
        nextId++;
        return newTask;
    }

    /**
     * Creates a new task list.
     *
     * @param name list name
     * @throws TodoAppException if name is invalid or duplicate
     */
    public void createList(String name) throws TodoAppException {
        TaskList newList = new TaskList(name);
        if (lists.containsKey(name)) {
            throw new TodoAppException(ERROR_UNIQUE_LIST_NAME);
        }
        lists.put(name, newList);
    }

    /**
     * Tags a task.
     *
     * @param taskId  ID of task to tag
     * @param tagName tag name
     * @return tagged task
     * @throws TodoAppException if task not found or tag invalid, or the task already has this tag
     */
    public Task tagTask(long taskId, String tagName) throws TodoAppException {
        Task task = getActiveTaskById(taskId);
        Tag tag = new Tag(tagName);
        if (task.hasTag(tag)) {
            throw new TodoAppException(ERROR_ALREADY_TAGGED);
        }
        task.addTag(tag);
        return task;
    }

    /**
     * Tags a list.
     *
     * @param listName name of list to tag
     * @param tagName  tag name
     * @throws TodoAppException if list not found or tag invalid, or the list already has this tag
     */
    public void tagList(String listName, String tagName) throws TodoAppException {
        TaskList list = getListByName(listName);
        Tag tag = new Tag(tagName);
        if (list.hasTag(tag)) {
            throw new TodoAppException(ERROR_ALREADY_TAGGED);
        }
        list.addTag(new Tag(tagName));
    }

    /**
     * Assigns parent task to child task.
     *
     * @param childTaskId  child task ID
     * @param parentTaskId parent task ID
     * @throws TodoAppException if tasks not found or cycle detected
     */
    public void assignParent(long childTaskId, long parentTaskId) throws TodoAppException {
        Task childTask = getActiveTaskById(childTaskId);
        Task parentTask = getActiveTaskById(parentTaskId);
        childTask.assignParent(parentTask);
        childTask.setTimeNow();
    }

    /**
     * Assigns task to list.
     *
     * @param taskId   task ID
     * @param listName list name
     * @throws TodoAppException if task or list not found or task is already in the list
     */
    public void assignList(long taskId, String listName) throws TodoAppException {
        Task task = getActiveTaskById(taskId);
        TaskList list = getListByName(listName);
        if (list.hasTask(task)) {
            throw new TodoAppException(ERROR_ALREADY_IN_LIST);
        }
        list.addTask(task);
        for (Task child : getChildren(task)) {
            list.addTask(child);
        }
    }

    /**
     * Toggles task status and all children.
     *
     * @param taskId task ID
     * @return count of children toggled
     * @throws TodoAppException if task not found
     */
    public int toggle(long taskId) throws TodoAppException {
        Task task = getActiveTaskById(taskId);
        boolean newStatus = !task.getIsOpen();
        task.setIsOpen(newStatus);
        int count = INITIAL_TASKS_COUNT;
        for (Task child : getChildren(task)) {
            if (!child.getIsDeleted()) {
                child.setIsOpen(newStatus);
                count++;
            }
        }
        return count;
    }

    /**
     * Changes task priority.
     *
     * @param task     task to modify
     * @param priority new priority
     */
    public void changePriority(Task task, TaskPriority priority) {
        task.setPriority(priority);
    }

    /**
     * Deletes task and all children.
     *
     * @param task task to delete
     * @return count of children deleted
     * @throws TodoAppException if already deleted
     */
    public int deleteTask(Task task) throws TodoAppException {
        if (task.getIsDeleted()) {
            throw new TodoAppException(ERROR_ALREADY_DELETED);
        }
        task.setIsDeleted(true);
        int count = INITIAL_TASKS_COUNT;
        for (Task child : getChildren(task)) {
            if (!child.getIsDeleted()) {
                child.setIsDeleted(true);
                count++;
            }
        }
        return count;
    }

    /**
     * Restores deleted task and all children.
     *
     * @param task task to restore
     * @return count of children restored
     * @throws TodoAppException if task not deleted
     */
    public int restoreTask(Task task) throws TodoAppException {
        if (!task.getIsDeleted()) {
            throw new TodoAppException(ERROR_RESTORE_EXISTING_TASK);
        }
        task.setIsDeleted(false);
        task.setTimeNow();
        Task parent = task.getParent();
        if (parent != null && parent.getIsDeleted()) {
            task.assignParent(null);
        }
        int count = INITIAL_TASKS_COUNT;
        for (Task child : getChildren(task)) {
            if (child.getIsDeleted()) {
                child.setIsDeleted(false);
                child.setTimeNow();
                count++;
            }
        }
        return count;
    }

    /**
     * Gets task by ID.
     *
     * @param id task ID
     * @return task
     * @throws TodoAppException if task not found
     */
    public Task getTaskById(long id) throws TodoAppException {
        if (!tasks.containsKey(id)) {
            throw new TodoAppException(ERROR_NO_SUCH_TASK);
        }
        return tasks.get(id);
    }

    /**
     * Gets active (non-deleted) task by ID.
     *
     * @param id task ID
     * @return task
     * @throws TodoAppException if task not found or deleted
     */
    public Task getActiveTaskById(long id) throws TodoAppException {
        Task task = getTaskById(id);
        if (task.getIsDeleted()) {
            throw new TodoAppException(ERROR_DELETED_TASK);
        }
        return task;
    }

    /**
     * Gets all open tasks with their parents.
     *
     * @return set of open tasks
     */
    public Set<Task> getOpenTasks() {
        Set<Task> foundTasks = tasks.values().stream().filter(Task::getIsOpen).collect(Collectors.toSet());
        Set<Task> copy = new HashSet<>(foundTasks);
        for (Task task : copy) {
            foundTasks.addAll(TaskTreeUtility.getAllParents(task));
        }
        return foundTasks;
    }

    /**
     * Gets all tasks in list including children.
     *
     * @param list task list
     * @return set of tasks
     */
    public Set<Task> getListTasks(TaskList list) {
        Set<Task> roots = list.getTasks();
        return enrichWithChildren(roots);
    }

    /**
     * Gets all tasks with tag including children.
     *
     * @param tag tag to search for
     * @return set of tagged tasks
     * @throws TodoAppException if no tasks found
     */
    public Set<Task> getTaggedTasks(Tag tag) throws TodoAppException {
        Set<Task> foundTasks = tasks.values().stream().filter(task -> task.hasTag(tag)).collect(Collectors.toSet());
        return enrichWithChildren(foundTasks);
    }

    /**
     * Searches tasks by name substring including children.
     *
     * @param query search query
     * @return set of matching tasks
     */
    public Set<Task> searchTasks(String query) {
        Set<Task> foundTasks = tasks.values().stream().filter(task -> task.containsSubstring(query)).collect(Collectors.toSet());
        return enrichWithChildren(foundTasks);
    }

    /**
     * Gets tasks with deadlines in upcoming days.
     *
     * @param date      start date
     * @param daysDelta days forward
     * @return set of upcoming tasks
     */
    public Set<Task> getUpcomingTasks(LocalDate date, int daysDelta) {
        return getTasksBetweenDates(date, date.plusDays(daysDelta));
    }

    /**
     * Gets tasks with deadlines before given date.
     *
     * @param date end date
     * @return set of tasks before date
     */
    public Set<Task> getBeforeTasks(ChronoLocalDate date) {
        Set<Task> foundTasks = tasks.values().stream().filter(task -> {
            LocalDate deadline = task.getDeadline();
            return deadline != null && !deadline.isAfter(date);
        }).collect(Collectors.toSet());
        return enrichWithChildren(foundTasks);
    }

    /**
     * Gets tasks with deadlines in date range.
     *
     * @param startDate start date (inclusive)
     * @param endDate   end date (inclusive)
     * @return set of tasks in range
     */
    public Set<Task> getTasksBetweenDates(ChronoLocalDate startDate, ChronoLocalDate endDate) {
        Set<Task> foundTasks = tasks.values().stream().filter(task -> {
            LocalDate deadline = task.getDeadline();
            return deadline != null && !deadline.isBefore(startDate) && !deadline.isAfter(endDate);
        }).collect(Collectors.toSet());
        return enrichWithChildren(foundTasks);
    }

    /**
     * Finds all duplicate tasks.
     *
     * @return sorted set of duplicate task IDs
     */
    public SortedSet<Long> getDuplicateTasks() {
        SortedSet<Long> duplicateTaskIds = new TreeSet<>();
        for (Task task1 : tasks.values()) {
            for (Task task2 : tasks.values()) {
                if (!task1.getIsDeleted() && !task2.getIsDeleted() && task1.isDuplicate(task2)) {
                    duplicateTaskIds.add(task1.getId());
                    duplicateTaskIds.add(task2.getId());
                }
            }
        }
        return duplicateTaskIds;
    }

    /**
     * Gets list by name.
     *
     * @param name list name
     * @return task list
     * @throws TodoAppException if list not found
     */
    public TaskList getListByName(String name) throws TodoAppException {
        if (!lists.containsKey(name)) {
            throw new TodoAppException(ERROR_NO_SUCH_LIST);
        }
        return lists.get(name);
    }

    /**
     * Gets all children of task.
     *
     * @param parentTask parent task
     * @return list of child tasks
     */
    public List<Task> getChildren(Task parentTask) {
        List<Task> children = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.isChild(parentTask)) {
                children.add(task);
            }
        }
        return children;
    }

    private Set<Task> enrichWithChildren(Set<Task> roots) {
        Set<Task> result = new HashSet<>(roots);
        for (Task task : roots) {
            result.addAll(getChildren(task));
        }
        return result;
    }
}