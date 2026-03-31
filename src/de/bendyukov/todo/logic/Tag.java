package de.bendyukov.todo.logic;

import java.util.Objects;

/**
 * Represents a tag for taggable entities.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
public class Tag {
    private static final String NAME_REGEX = "^[a-zA-Z0-9]+$"; // Name contains 1 or more letters or digits and nothing else
    private static final String ERROR_INVALID_NAME = "Only letters A-Z and a-z and digits are allowed";

    private final String name;

    /**
     * Creates a tag with the given name.
     *
     * @param name tag name (letters and digits only)
     * @throws TodoAppException if name contains invalid characters
     */
    public Tag(String name) throws TodoAppException {
        if (!name.matches(NAME_REGEX)) {
            throw new TodoAppException(ERROR_INVALID_NAME);
        }

        this.name = name;
    }

    /**
     * Gets the tag name.
     *
     * @return tag name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks equality based on tag name.
     *
     * @param object object to compare
     * @return true if tags have same name
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Tag)) {
            return false;
        }

        Tag other = (Tag) object;

        return name.equals(other.getName());
    }

    /**
     * Returns hash code based on name.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
