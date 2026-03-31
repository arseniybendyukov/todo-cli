package de.bendyukov.todo.logic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Tree node representing a task and its children.
 *
 * @author udkcf
 * @version 1.0
 */
public class TaskTreeNode {
    private final Task task;
    private List<TaskTreeNode> children = new ArrayList<>();

    /**
     * Creates a tree node for the given task.
     *
     * @param task task to wrap
     */
    public TaskTreeNode(Task task) {
        this.task = task;
    }

    /**
     * Gets the task of this node.
     *
     * @return task
     */
    public Task getTask() {
        return task;
    }

    /**
     * Adds a child node.
     *
     * @param child child node to add
     */
    public void addChild(TaskTreeNode child) {
        children.add(child);
    }

    /**
     * Checks if this node has at least one child.
     *
     * @return true if this has at least one child
     */
    public boolean hasAnyChild() {
        return !children.isEmpty();
    }

    /**
     * Gets tasks of all child nodes.
     *
     * @return list of child tasks
     */
    public List<Task> getChildTasks() {
        return children.stream().map(TaskTreeNode::getTask).toList();
    }

    /**
     * Gets all child nodes.
     *
     * @return copy of child nodes list
     */
    public List<TaskTreeNode> getChildNodes() {
        return new ArrayList<>(children);
    }

    /**
     * Sets the child nodes.
     *
     * @param children new child nodes
     */
    public void setChildren(List<TaskTreeNode> children) {
        this.children = new ArrayList<>(children);
    }

    /**
     * Gets when task was added.
     *
     * @return task creation timestamp
     */
    public Instant getTaskAddedAt() {
        return task.getAddedAt();
    }
}
