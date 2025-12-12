package org.estg.ed.Content;

import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Players.Player;

/**
 * Represents the abstract base for any interactive content found within a maze division.
 *
 * This class serves as the parent for specific game challenges, such as Enigmas and Levers.
 * It manages the common state of whether a challenge has been solved or activated.
 *
 * @author Mariana Barreto 8240344
 */
public abstract class Content {
    private boolean solved;

    public Content() {
        this.solved = false;
    }

    /**
     * Retrieves the specific type of this content.
     *
     * This method allows other components to identify if the content is a type defined in the DivisionType enum.
     *
     * @return The specific DivisionType associated with this content subclass.
     */
    public abstract DivisionType getType();

    /**
     * Executes the interaction between the player and the content.
     * 
     * This method must be implemented by subclasses with the specific logic
     * for each content type.
     * 
     * @param p the current player
     *
     * @return true if the interaction was successful, false otherwise
     */
    public abstract boolean interactType(Player p);

    /**
     * Indicates if the content has already been solved.
     *
     * @return true if solved, false otherwise
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * Changes the solved state of this content.
     * 
     * Only subclasses should use this method.
     *
     * @param isSolved
     */
    protected void setSolved(boolean isSolved) {
        this.solved = isSolved;
    }


    /**
     * Returns a string representation of this Content instance.
     *
     * @return A formatted string describing the content's current state.
     */
    @Override
    public String toString() {
        return "Content [solved=" + solved + ", toString()=" + super.toString() + "]";
    }

}
