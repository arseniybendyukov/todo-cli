package de.bendyukov.todo.logic;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Utility for building and displaying task trees.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public final class TaskTreeUtility {
    private static final String TAB = "  ";
    private static final int MIN_DEPTH = 0;
    private static final String LIST_BULLET = "- ";
    private static final String TASK_OPENED = "[ ] ";
    private static final String TASK_CLOSED = "[x] ";
    private static final String PRIORITY_BEFORE = " [";
    private static final String PRIORITY_AFTER = "]";
    private static final String METADATA_SEPARATOR = ":";
    private static final String TAGS_BEFORE = " (";
    private static final String TAGS_SEPARATOR = ", ";
    private static final String TAGS_AFTER = ")";
    private static final String DEADLINE_INDICATOR = " --> ";
    private static final String EMPTY_TREE = "";
    private static final int INITIAL_DEPTH = 0;
    private static final int CHILD_DEPTH_OFFSET = 1;
    private static final int GRANDCHILD_DEPTH_OFFSET = 2;

    private TaskTreeUtility() {
    }

    private static String displayTree(List<TaskTreeNode> tree, int depth) {
        if (tree.isEmpty()) {
            return EMPTY_TREE;
        }

        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        for (TaskTreeNode node : tree) {
            joiner.add(displayTask(node.getTask(), depth));
            List<TaskTreeNode> subtree = node.getChildNodes();

            for (TaskTreeNode child : subtree) {
                joiner.add(displayTask(child.getTask(), depth + CHILD_DEPTH_OFFSET));
                String grandChildren = displayTree(child.getChildNodes(), depth + GRANDCHILD_DEPTH_OFFSET);
                if (!grandChildren.isEmpty()) {
                    joiner.add(grandChildren);
                }
            }
        }

        return joiner.toString();
    }

    private static String displayTask(Task task, int depth) {
        StringBuilder output = new StringBuilder();

        output.append(TAB.repeat(Math.max(MIN_DEPTH, depth)));

        output.append(LIST_BULLET);

        if (task.getIsOpen()) {
            output.append(TASK_OPENED);
        } else {
            output.append(TASK_CLOSED);
        }

        output.append(task.getName());

        if (task.getPriority() != null) {
            output.append(displayPriority(task.getPriority()));
        }

        Set<Tag> tags = task.getTags();
        LocalDate deadline = task.getDeadline();

        if (!tags.isEmpty() || deadline != null) {
            output.append(METADATA_SEPARATOR);
        }

        if (!tags.isEmpty()) {
            output.append(displayTags(tags));
        }

        if (deadline != null) {
            output.append(displayDeadline(deadline));
        }

        return output.toString();
    }

    private static String displayPriority(TaskPriority priority) {
        return PRIORITY_BEFORE + priority.getCode() + PRIORITY_AFTER;
    }

    private static String displayDeadline(ChronoLocalDate deadline) {
        return DEADLINE_INDICATOR + Task.formatDate(deadline);
    }

    private static String displayTags(Collection<Tag> tags) {
        StringJoiner tagsJoin = new StringJoiner(TAGS_SEPARATOR);
        for (Tag tag : tags) {
            tagsJoin.add(tag.getName());
        }
        return TAGS_BEFORE + tagsJoin + TAGS_AFTER;
    }

    /**
     * Builds and formats a sorted task tree.
     *
     * @param tasks collection of tasks to display
     * @return formatted tree string
     */
    public static String buildSortedTree(Collection<Task> tasks) {
        if (tasks == null) {
            return EMPTY_TREE;
        }

        List<Task> activeTasks = tasks.stream().filter(task -> !task.getIsDeleted()).toList();

        List<Task> roots = new ArrayList<>();
        for (Task task : activeTasks) {
            if (task.getParent() == null || !activeTasks.contains(task.getParent())) {
                roots.add(task);
            }
        }

        return displayTree(buildSortedTree(activeTasks, roots), INITIAL_DEPTH);
    }

    private static List<TaskTreeNode> buildSortedTree(List<Task> tasks, List<Task> currentLevel) {
        List<TaskTreeNode> nodes = new ArrayList<>();

        for (Task task : currentLevel) {
            TaskTreeNode node = new TaskTreeNode(task);

            for (Task potentialChild : tasks) {
                if (potentialChild.isDirectChild(task)) {
                    node.addChild(new TaskTreeNode(potentialChild));
                }
            }

            if (node.hasAnyChild()) {
                node.setChildren(buildSortedTree(tasks, node.getChildTasks()));
            }

            nodes.add(node);
        }

        nodes.sort(Comparator.comparing((TaskTreeNode node) -> {
            TaskPriority priority = node.getTask().getPriority();
            return priority == null ? Integer.MAX_VALUE : priority.getPriority();
        }).thenComparing(TaskTreeNode::getTaskAddedAt, Comparator.nullsLast(Comparator.naturalOrder())));

        return nodes;
    }

    /**
     * Returns the list of all parents of the given task.
     *
     * @param task the task to list parents of
     * @return the list of parents of the task
     */
    public static List<Task> getAllParents(Task task) {
        List<Task> parents = new ArrayList<>();
        Task currentTask = task;
        while (true) {
            Task currentParent = currentTask.getParent();
            if (currentParent != null) {
                parents.add(currentParent);
                currentTask = currentParent;
            } else {
                break;
            }
        }
        return parents;
    }
}
