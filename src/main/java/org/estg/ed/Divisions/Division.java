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

    private DivisionType dt;

    private Content content;

    /**
     * Constructs a new Division object with the specified unique identifier.
     * Initializes with no content.
     *
     * @param id The unique integer identifier for this division.
     */
    public Division(int id, DivisionType dt) {
        this.id = id;
        this.dt = dt;
        this.content = null;
    }

    public Division(int id) {
        this.id = id;
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

    public DivisionType getDt() {
        return dt;
    }

    public void setDt(DivisionType dt) {
        this.dt = dt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Division{");
        sb.append("id=").append(id);
        sb.append(", dt=").append(dt);
        sb.append(", Content=").append(content);
        sb.append('}');
        return sb.toString();
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Content getContent() {
        return content;
    }
}