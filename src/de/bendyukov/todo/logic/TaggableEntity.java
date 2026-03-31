package de.bendyukov.todo.logic;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Base class for entities that can be tagged.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public abstract class TaggableEntity {
    private final Set<Tag> tags = new LinkedHashSet<>();

    /**
     * Adds a tag to this entity.
     *
     * @param tag tag to add
     */
    public void addTag(Tag tag) {
        if (tag != null) {
            tags.add(tag);
        }
    }

    /**
     * Gets all tags of this entity.
     *
     * @return copy of tag set
     */
    public Set<Tag> getTags() {
        return new LinkedHashSet<>(tags);
    }

    /**
     * Checks if task has given tag.
     *
     * @param tag tag to check
     * @return true if task has tag
     */
    public boolean hasTag(Tag tag) {
        return tags.contains(tag);
    }
}
