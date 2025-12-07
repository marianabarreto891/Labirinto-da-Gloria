package org.estg.ed.Divisions;

import org.estg.ed.Content.Content;

/**
 * Represents a vertex (node) in the game map network.
 * 
 * @author Mariana Barreto 8240344
 */
public class Division {

    /**
     * The unique identifier for this division.
     * This ID corresponds to the value defined in the JSON map file.
     */
    private final int id;

    /**
     * The specific game content (challenge/item) located in this division.
     */
    private Content content;

    private DivisionType dt;

    /**
     * Constructs a new Division object with the specified unique identifier.
     * Initializes with no content.
     *
     * @param id The unique integer identifier for this division.
     */
    public Division(int id, Content content, DivisionType dt) {
        this.id = id;
        this.content = null;
        this.dt = dt;
    }

    public Division(int id) {
        this.id = id;
    }

    /**
     * Sets the content object associated with this instance.
     *
     * @param content The Content object to be set.
     */
    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * Checks if a content object has been set for this instance.
     *
     * @return true if the content is not null; false otherwise.
     */
    public boolean hasContent() {
        return this.content != null;
    }

    /**
     * Retrieves the unique identifier of this division.
     *
     * @return The integer ID of the division.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the content object associated with this instance.
     *
     * @return The current Content object, or null if no content has been set.
     */
    public Content getContent() {
        return content;
    }

    /**
     * Returns a string representation of the Division.
     *
     * @return A formatted string describing the division.
     */
    @Override
    public String toString() {
        String contentStr = (content == null) ? "Empty" : "Has Challenge (" + content.getIdContent() + ")";
        return "Division " + id + " [" + contentStr + "]";
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Essential for the Network class to find this Division in its internal array.
     *
     * @param obj the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Division division = (Division) obj;
        return id == division.id;
    }
}